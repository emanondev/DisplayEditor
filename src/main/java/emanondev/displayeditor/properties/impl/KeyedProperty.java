package emanondev.displayeditor.properties.impl;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyedProperty<E, S extends Keyed> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;

    /**
     * Assumes the registry is not empty
     */
    @SuppressWarnings("unchecked")
    public KeyedProperty(@NotNull String name,
                         @NotNull Class<E> entityClass,
                         @NotNull Function<E, S> getter,
                         @NotNull BiConsumer<E, S> setter,
                         @NotNull Supplier<S> defaultProvider,
                         @NotNull Registry<S> registry) {
        this(name, entityClass, (Class<S>) registry.stream().findFirst()
                .orElseThrow().getClass(), getter, setter, defaultProvider, registry);
    }

    @SuppressWarnings("UnstableApiUsage")
    public KeyedProperty(@NotNull String name,
                         @NotNull Class<E> entityClass,
                         @NotNull Class<S> valueClass,
                         @NotNull Function<E, S> getter,
                         @NotNull BiConsumer<E, S> setter,
                         @NotNull Supplier<S> defaultProvider,
                         @NotNull Registry<S> registry) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value.getKey().toString());
        this.fromMap = (map) -> {
            String[] split = map.get(name()) instanceof String value ? value.split(":") : null;
            S value = null;
            if (!(split == null || split.length < 2))
                value = registry.get(new NamespacedKey(split[0], split[1]));
            if (value == null)
                value = defaultProvider.get();
            return value;
        };
    }

    protected @NotNull Function<Map<String, Object>, S> getFromMap() {
        return fromMap;
    }

    protected @NotNull BiConsumer<S, Map<String, Object>> setToMap() {
        return toMap;
    }

    @Nullable
    @Override
    public S getFromMap(@NotNull Map<String, Object> map) {
        return fromMap.apply(map);
    }

    @Override
    public void setToMap(@NotNull Map<String, Object> map, @Nullable S value) {
        toMap.accept(value, map);
    }

}

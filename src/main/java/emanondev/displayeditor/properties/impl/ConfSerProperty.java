package emanondev.displayeditor.properties.impl;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfSerProperty<E, S extends ConfigurationSerializable> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;

    @SuppressWarnings("unchecked")
    public ConfSerProperty(@NotNull String name,
                           @NotNull Class<E> entityClass,
                           @NotNull Class<S> valueClass,
                           @NotNull Function<E, S> getter,
                           @NotNull BiConsumer<E, S> setter,
                           @NotNull Supplier<S> defaultProvider) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value);
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (valueClass.isInstance(value))
                return (S) value;//TODO message if not instance nor null
            return defaultProvider.get();
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

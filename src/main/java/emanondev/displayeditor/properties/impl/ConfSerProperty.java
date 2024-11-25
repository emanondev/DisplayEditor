package emanondev.displayeditor.properties.impl;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull Function<Map<String, Object>, S> getFromMap() {
        return fromMap;
    }

    @Override
    public @NotNull BiConsumer<S, Map<String, Object>> getToMap() {
        return toMap;
    }

}

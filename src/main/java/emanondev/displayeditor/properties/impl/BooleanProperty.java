package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BooleanProperty<E> extends AProperty<E, Boolean> {

    private final BiConsumer<Boolean, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, Boolean> fromMap;

    public BooleanProperty(@NotNull String name,
                           @NotNull Class<E> entityClass,
                           @NotNull Function<E, Boolean> getter,
                           @NotNull BiConsumer<E, Boolean> setter,
                           @NotNull Supplier<Boolean> defaultProvider) {
        super(name, entityClass, Boolean.class, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value);
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (value instanceof Boolean bvalue)
                return bvalue;//TODO
            return defaultProvider.get();
        };
    }

    @Override
    public @NotNull Function<Map<String, Object>, Boolean> getFromMap() {
        return fromMap;
    }

    @Override
    public @NotNull BiConsumer<Boolean, Map<String, Object>> getToMap() {
        return toMap;
    }

}

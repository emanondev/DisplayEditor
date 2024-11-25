package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UuidProperty<E> extends AProperty<E, UUID> {

    private final BiConsumer<UUID, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, UUID> fromMap;

    public UuidProperty(@NotNull String name,
                        @NotNull Class<E> entityClass,
                        @NotNull Function<E, UUID> getter,
                        @NotNull BiConsumer<E, UUID> setter) {
        this(name, entityClass, getter, setter, () -> null);
    }

    public UuidProperty(@NotNull String name,
                        @NotNull Class<E> entityClass,
                        @NotNull Function<E, UUID> getter,
                        @NotNull BiConsumer<E, UUID> setter,
                        @NotNull Supplier<UUID> defaultProvider) {
        super(name, entityClass, UUID.class, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value == null ? null : value.toString());
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (value instanceof String strValue)
                return UUID.fromString(strValue);//TODO
            return defaultProvider.get();
        };
    }

    @Override
    public @NotNull Function<Map<String, Object>, UUID> getFromMap() {
        return fromMap;
    }

    @Override
    public @NotNull BiConsumer<UUID, Map<String, Object>> getToMap() {
        return toMap;
    }

}

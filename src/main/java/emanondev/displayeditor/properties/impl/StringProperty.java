package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StringProperty<E> extends AProperty<E, String> {

    private final BiConsumer<String, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, String> fromMap;

    public StringProperty(@NotNull String name,
                          @NotNull Class<E> entityClass,
                          @NotNull Function<E, String> getter,
                          @NotNull BiConsumer<E, String> setter,
                          @NotNull Supplier<String> defaultProvider) {
        super(name, entityClass, String.class, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value);
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (value instanceof String strValue)
                return strValue;
            return defaultProvider.get();
        };
    }

    protected @NotNull Function<Map<String, Object>, String> getFromMap() {
        return fromMap;
    }

    protected @NotNull BiConsumer<String, Map<String, Object>> setToMap() {
        return toMap;
    }

    @Nullable
    @Override
    public String getFromMap(@NotNull Map<String, Object> map) {
        return fromMap.apply(map);
    }

    @Override
    public void setToMap(@NotNull Map<String, Object> map, @Nullable String value) {
        toMap.accept(value, map);
    }

}

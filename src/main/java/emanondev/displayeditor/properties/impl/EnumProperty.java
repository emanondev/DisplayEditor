package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.DisplayEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnumProperty<E, S extends Enum<S>> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;

    public EnumProperty(@NotNull String name,
                        @NotNull Class<E> entityClass,
                        @NotNull Class<S> valueClass,
                        @NotNull Function<E, S> getter,
                        @NotNull BiConsumer<E, S> setter,
                        @NotNull Supplier<S> defaultProvider) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value.name());
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (value instanceof String strValue)
                try {
                    return Enum.valueOf(valueClass, strValue);
                } catch (IllegalArgumentException e) {
                    DisplayEditor.get().log("<red>Error <white>Unable to find Enum value '<yellow>"
                            + strValue + "</yellow>' for enum class '<yellow>"
                            + valueClass.getSimpleName() + "'</yellow>");
                }
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

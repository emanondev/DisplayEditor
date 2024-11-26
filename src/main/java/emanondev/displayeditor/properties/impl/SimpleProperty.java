package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleProperty<E, S> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;

    public SimpleProperty(@NotNull String name,
                          @NotNull Class<E> entityClass,
                          @NotNull Class<S> valueClass,
                          @NotNull Function<E, S> getter,
                          @NotNull BiConsumer<E, S> setter,
                          @NotNull Supplier<S> defaultProvider,
                          @NotNull BiConsumer<S, Map<String, Object>> toMap,
                          @NotNull Function<Map<String, Object>, S> fromMap) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);
        this.toMap = toMap;
        this.fromMap = fromMap;
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


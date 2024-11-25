package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;

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

    public @NotNull Function<Map<String, Object>, S> getFromMap() {
        return fromMap;
    }

    public @NotNull BiConsumer<S, Map<String, Object>> getToMap() {
        return toMap;
    }

}


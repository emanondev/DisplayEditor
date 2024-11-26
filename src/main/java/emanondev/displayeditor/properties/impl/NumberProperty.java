package emanondev.displayeditor.properties.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NumberProperty<E, S extends Number> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;

    @SuppressWarnings("unchecked")
    private NumberProperty(@NotNull String name,
                           @NotNull Class<E> entityClass,
                           @NotNull Class<S> valueClass,
                           @NotNull Function<E, S> getter,
                           @NotNull BiConsumer<E, S> setter,
                           @NotNull Supplier<S> defaultProvider) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);

        if (!(valueClass.equals(Integer.class)
                || valueClass.equals(Double.class)
                || valueClass.equals(Float.class)
                || valueClass.equals(Long.class)
                || valueClass.equals(Byte.class)
                || valueClass.equals(Short.class))) {
            throw new IllegalArgumentException("<red>Error <white>Unable to handle Number Class '<yellow>"
                    + valueClass.getName() + "</yellow>'");
        }

        this.toMap = (value, map) -> map.put(name(), value);
        this.fromMap = (map) -> {
            Object value = map.get(name());
            if (value instanceof Number number) {
                if (valueClass.equals(Integer.class)) {
                    return (S) (Integer) number.intValue();
                } else if (valueClass.equals(Double.class)) {
                    return (S) (Double) number.doubleValue();
                } else if (valueClass.equals(Float.class)) {
                    return (S) (Float) number.floatValue();
                } else if (valueClass.equals(Long.class)) {
                    return (S) (Long) number.longValue();
                } else if (valueClass.equals(Byte.class)) {
                    return (S) (Byte) number.byteValue();
                } else if (valueClass.equals(Short.class)) {
                    return (S) (Short) number.shortValue();
                }
            }
            return defaultProvider.get();
        };
    }

    public static <E> NumberProperty<E, Integer> fromInt(@NotNull String name,
                                                         @NotNull Class<E> entityClass,
                                                         @NotNull Function<E, Integer> getter,
                                                         @NotNull BiConsumer<E, Integer> setter,
                                                         @NotNull Supplier<Integer> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Integer.class, getter, setter, defaultProvider);
    }

    public static <E> NumberProperty<E, Double> fromDouble(@NotNull String name,
                                                           @NotNull Class<E> entityClass,
                                                           @NotNull Function<E, Double> getter,
                                                           @NotNull BiConsumer<E, Double> setter,
                                                           @NotNull Supplier<Double> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Double.class, getter, setter, defaultProvider);
    }

    public static <E> NumberProperty<E, Float> fromFloat(@NotNull String name,
                                                         @NotNull Class<E> entityClass,
                                                         @NotNull Function<E, Float> getter,
                                                         @NotNull BiConsumer<E, Float> setter,
                                                         @NotNull Supplier<Float> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Float.class, getter, setter, defaultProvider);
    }

    public static <E> NumberProperty<E, Byte> fromByte(@NotNull String name,
                                                       @NotNull Class<E> entityClass,
                                                       @NotNull Function<E, Byte> getter,
                                                       @NotNull BiConsumer<E, Byte> setter,
                                                       @NotNull Supplier<Byte> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Byte.class, getter, setter, defaultProvider);
    }

    public static <E> NumberProperty<E, Long> fromLong(@NotNull String name,
                                                       @NotNull Class<E> entityClass,
                                                       @NotNull Function<E, Long> getter,
                                                       @NotNull BiConsumer<E, Long> setter,
                                                       @NotNull Supplier<Long> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Long.class, getter, setter, defaultProvider);
    }

    public static <E> NumberProperty<E, Short> fromShort(@NotNull String name,
                                                         @NotNull Class<E> entityClass,
                                                         @NotNull Function<E, Short> getter,
                                                         @NotNull BiConsumer<E, Short> setter,
                                                         @NotNull Supplier<Short> defaultProvider) {
        return new NumberProperty<>(name, entityClass, Short.class, getter, setter, defaultProvider);
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
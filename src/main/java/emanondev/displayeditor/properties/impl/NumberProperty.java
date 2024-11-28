package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.properties.Property;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.editors.APropertyEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    protected NumberProperty(@NotNull String name,
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

    @Override
    public PropertyEditor getPropertyEditor(E entity, Player player) {
        if (getValueClass().equals(Double.class))
            return new DoublePropertyEditor(entity,player);
        if (getValueClass().equals(Float.class))
            return new FloatPropertyEditor(entity,player);
        if (getValueClass().equals(Long.class))
            return new LongPropertyEditor(entity,player);
        if (getValueClass().equals(Integer.class))
            return new IntegerPropertyEditor(entity,player);
        if (getValueClass().equals(Short.class))
            return new ShortPropertyEditor(entity,player);
        if (getValueClass().equals(Byte.class))
            return new BytePropertyEditor(entity,player);
        return null;
    }

    class DoublePropertyEditor extends APropertyEditor<E, Double> {
        private int editorExponent = 0;

        public DoublePropertyEditor(E entity, Player player) {
            super((Property<E, Double>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Double value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0D;
                    property.setToEntity(entity, value+Math.pow(10,editorExponent));
                    return true;
                }
                case RIGHT -> {
                    Double value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0D;
                    property.setToEntity(entity, value-Math.pow(10,editorExponent));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent+1,Double.MAX_EXPONENT-1);
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent-1,Double.MIN_EXPONENT+1);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Double value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

    class FloatPropertyEditor extends APropertyEditor<E, Float> {
        private int editorExponent = 0;

        public FloatPropertyEditor(E entity, Player player) {
            super((Property<E, Float>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Float value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0F;
                    property.setToEntity(entity, (float) (value+Math.pow(10,editorExponent)));
                    return true;
                }
                case RIGHT -> {
                    Float value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0F;
                    property.setToEntity(entity, (float) (value-Math.pow(10,editorExponent)));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent,Float.MAX_EXPONENT-1);
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent,Float.MIN_EXPONENT+1);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Float value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

    class LongPropertyEditor extends APropertyEditor<E, Long> {
        private int editorExponent = 0;

        public LongPropertyEditor(E entity, Player player) {
            super((Property<E, Long>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Long value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0L;
                    property.setToEntity(entity, (long) (value+Math.pow(10,editorExponent)));
                    return true;
                }
                case RIGHT -> {
                    Long value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0L;
                    property.setToEntity(entity, (long) (value-Math.pow(10,editorExponent)));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent,(int) Math.log10(Long.MAX_VALUE-1));
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent,0);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Long value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

    class BytePropertyEditor extends APropertyEditor<E, Byte> {
        private int editorExponent = 0;

        public BytePropertyEditor(E entity, Player player) {
            super((Property<E, Byte>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Byte value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (byte) (value+Math.pow(10,editorExponent)));
                    return true;
                }
                case RIGHT -> {
                    Byte value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (byte) (value-Math.pow(10,editorExponent)));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent,(int) Math.log10(Byte.MAX_VALUE-1));
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent,0);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Byte value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

    class ShortPropertyEditor extends APropertyEditor<E, Short> {
        private int editorExponent = 0;

        public ShortPropertyEditor(E entity, Player player) {
            super((Property<E, Short>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Short value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (short) (value+Math.pow(10,editorExponent)));
                    return true;
                }
                case RIGHT -> {
                    Short value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (short) (value-Math.pow(10,editorExponent)));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent,(int) Math.log10(Short.MAX_VALUE-1));
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent,0);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Short value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

    class IntegerPropertyEditor extends APropertyEditor<E, Integer> {
        private int editorExponent = 0;

        public IntegerPropertyEditor(E entity, Player player) {
            super((Property<E, Integer>) NumberProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT -> {
                    Integer value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (int) (value+Math.pow(10,editorExponent)));
                    return true;
                }
                case RIGHT -> {
                    Integer value = property.getFromEntity(entity);
                    if (value==null)
                        value = 0;
                    property.setToEntity(entity, (int) (value-Math.pow(10,editorExponent)));
                    return true;
                }
                case SHIFT_LEFT -> {
                    editorExponent = Math.min(editorExponent,(int) Math.log10(Integer.MAX_VALUE-1));
                    return true;
                }
                case SHIFT_RIGHT -> {
                    editorExponent = Math.max(editorExponent,0);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    property.setToEntity(entity, property.getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            Integer value = property.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null": String.valueOf(value),
                    "%change%", String.valueOf(Math.pow(10,editorExponent)),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }



}
package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.editors.APropertyEditor;
import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

    @Override
    public PropertyEditor getPropertyEditor(E e, Player p) {
        return new EnumPropertyEditor(e, p);
    }

    class EnumPropertyEditor extends APropertyEditor<E, S> {

        public EnumPropertyEditor(E entity, Player player) {
            super(EnumProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT, SHIFT_LEFT -> {
                    S value = EnumProperty.this.getFromEntity(entity);
                    List<S> list = Arrays.stream(getValueClass().getEnumConstants()).sorted(
                            Comparator.comparing(Enum::name)).toList();
                    int index = (list.indexOf(value) - (event.isShiftClick() ? 10 : 1) + list.size()) % list.size();
                    EnumProperty.this.setToEntity(entity, list.get(index));
                    return true;
                }
                case SHIFT_RIGHT, RIGHT -> {
                    S value = EnumProperty.this.getFromEntity(entity);
                    List<S> list = Arrays.stream(getValueClass().getEnumConstants()).sorted(
                            Comparator.comparing(Enum::name)).toList();
                    int index = (list.indexOf(value) + (event.isShiftClick() ? 10 : 1)) % list.size();
                    EnumProperty.this.setToEntity(entity, list.get(index));
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    EnumProperty.this.setToEntity(entity, getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            S value = EnumProperty.this.getFromEntity(entity);
            return new String[]{"%value%", value==null?"null":value.name(),
                    "%value_color%", value == null ? "<yellow>" : "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }
}

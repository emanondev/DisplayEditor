package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.editors.APropertyEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    protected @NotNull Function<Map<String, Object>, Boolean> getFromMap() {
        return fromMap;
    }

    protected @NotNull BiConsumer<Boolean, Map<String, Object>> setToMap() {
        return toMap;
    }

    @Nullable
    @Override
    public Boolean getFromMap(@NotNull Map<String, Object> map) {
        return fromMap.apply(map);
    }

    @Override
    public void setToMap(@NotNull Map<String, Object> map, @Nullable Boolean value) {
        toMap.accept(value, map);
    }

    @Override
    public PropertyEditor getPropertyEditor(E e, Player p) {
        return new BooleanPropertyEditor(e, p);
    }

    class BooleanPropertyEditor extends APropertyEditor<E, Boolean> {

        public BooleanPropertyEditor(E entity, Player player) {
            super(BooleanProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT, SHIFT_LEFT, SHIFT_RIGHT, RIGHT -> {
                    Boolean value = BooleanProperty.this.getFromEntity(entity);
                    BooleanProperty.this.setToEntity(entity, value == null || !value);
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    BooleanProperty.this.setToEntity(entity, getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected Material getMaterial() {
            Boolean value = BooleanProperty.this.getFromEntity(entity);
            String path = getPath() + (value == null ? ".null" : (value ? ".true" : ".false") + ".material");
            return DisplayEditor.get().getConfig("gui.yml")
                    .loadEnum(path, value == null ? Material.YELLOW_WOOL : value ?
                            Material.LIME_WOOL : Material.RED_WOOL, Material.class);
        }

        @Override
        protected Boolean getGlow() {
            Boolean value = BooleanProperty.this.getFromEntity(entity);
            String path = getPath() + (value == null ? ".null" : (value ? ".true" : ".false") + ".glow");
            return DisplayEditor.get().getConfig("gui.yml")
                    .loadBoolean(path, null);
        }

        @Override
        protected Integer getModelData() {
            Boolean value = BooleanProperty.this.getFromEntity(entity);
            String path = getPath() + (value == null ? ".null" : (value ? ".true" : ".false") + ".modeldata");
            return DisplayEditor.get().getConfig("gui.yml")
                    .loadInteger(path, null);
        }

        @Override
        protected String[] getPlaceholders() {
            Boolean value = BooleanProperty.this.getFromEntity(entity);
            return new String[]{"%value%", "" + value,
                    "%value_color%", value == null ? "<yellow>" : value ? "<green>" : "<red>",
                    "%value_color_end%", value == null ? "</yellow>" : value ? "</green>" : "</red>"};
        }
    }
}

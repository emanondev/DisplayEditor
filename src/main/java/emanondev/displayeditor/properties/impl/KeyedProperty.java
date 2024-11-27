package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.properties.editors.APropertyEditor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyedProperty<E, S extends Keyed> extends AProperty<E, S> {

    private final BiConsumer<S, Map<String, Object>> toMap;
    private final Function<Map<String, Object>, S> fromMap;
    private final Registry<S> registry;

    /**
     * Assumes the registry is not empty
     */
    @SuppressWarnings("unchecked")
    public KeyedProperty(@NotNull String name,
                         @NotNull Class<E> entityClass,
                         @NotNull Function<E, S> getter,
                         @NotNull BiConsumer<E, S> setter,
                         @NotNull Supplier<S> defaultProvider,
                         @NotNull Registry<S> registry) {
        this(name, entityClass, (Class<S>) registry.stream().findFirst()
                .orElseThrow().getClass(), getter, setter, defaultProvider, registry);
    }

    @SuppressWarnings("UnstableApiUsage")
    public KeyedProperty(@NotNull String name,
                         @NotNull Class<E> entityClass,
                         @NotNull Class<S> valueClass,
                         @NotNull Function<E, S> getter,
                         @NotNull BiConsumer<E, S> setter,
                         @NotNull Supplier<S> defaultProvider,
                         @NotNull Registry<S> registry) {
        super(name, entityClass, valueClass, getter, setter, defaultProvider);
        this.toMap = (value, map) -> map.put(name(), value == null ? null : value.getKey().toString());
        this.fromMap = (map) -> {
            String[] split = map.get(name()) instanceof String value ? value.split(":") : null;
            S value = null;
            if (!(split == null || split.length < 2))
                value = registry.get(new NamespacedKey(split[0], split[1]));
            if (value == null)
                value = defaultProvider.get();
            return value;
        };
        this.registry = registry;
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
        return new KeyedPropertyEditor(e, p);
    }

    class KeyedPropertyEditor extends APropertyEditor<E, S> {

        public KeyedPropertyEditor(E entity, Player player) {
            super(KeyedProperty.this, player, entity);
        }

        @Override
        public boolean handleClick(InventoryClickEvent event) {
            switch (event.getClick()) {
                case LEFT, SHIFT_LEFT -> {
                    S value = KeyedProperty.this.getFromEntity(entity);
                    List<S> list = registry.stream().sorted(Comparator.comparing(k -> k.getKey().toString())).toList();
                    int index = (list.indexOf(value) - (event.isShiftClick() ? 10 : 1) + list.size()) % list.size();
                    KeyedProperty.this.setToEntity(entity, list.get(index));
                    return true;
                }
                case SHIFT_RIGHT, RIGHT -> {
                    S value = KeyedProperty.this.getFromEntity(entity);
                    List<S> list = registry.stream().sorted(Comparator.comparing(k -> k.getKey().toString())).toList();
                    int index = (list.indexOf(value) + (event.isShiftClick() ? 10 : 1)) % list.size();
                    KeyedProperty.this.setToEntity(entity, list.get(index));
                    return true;
                }
                case SWAP_OFFHAND, CREATIVE, DROP, CONTROL_DROP -> {
                    KeyedProperty.this.setToEntity(entity, getDefault());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String[] getPlaceholders() {
            S value = KeyedProperty.this.getFromEntity(entity);
            return new String[]{"%value%", "" + value,
                    "%value_color%", value == null ? "<yellow>" :  "<aqua>",
                    "%value_color_end%", value == null ? "</yellow>" : "</aqua>"};
        }
    }

}

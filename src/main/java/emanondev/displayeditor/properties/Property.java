package emanondev.displayeditor.properties;

import org.bukkit.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Property<E, S> extends Keyed {

    @NotNull
    String name();

    @NotNull
    Class<E> getEntityClass();

    @NotNull
    Class<S> getValueClass();

    @Nullable
    S getFromEntity(@NotNull E e);

    void setToEntity(@NotNull E e, @Nullable S s);

    @Nullable
    S getDefault();

    void applyToEntity(@NotNull E e, @NotNull Map<String, Object> valueContainer, @Nullable Context context);

    void applyToMap(@NotNull E e, @NotNull Map<String, Object> valueContainer);


    @Nullable
    S getFromMap(@NotNull Map<String, Object> map);

    void setToMap(@NotNull Map<String, Object> map, @Nullable S s);

    PropertyEditor getPropertyEditor(E entity, Player player);

    default PropertyEditor getPropertyEditorRaw(Entity entity, Player player){
        try {
            return getPropertyEditor((E) entity, player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


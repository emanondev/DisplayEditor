package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.Context;
import org.bukkit.Keyed;
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

}


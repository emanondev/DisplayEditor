package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.Context;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AProperty<E, S> implements Property<E, S>, Keyed {

    private final Class<E> entityClass;
    private final Class<S> valueClass;
    private final Function<E, S> getter;
    private final BiConsumer<E, S> setter;
    private final Supplier<S> defaultProvider;
    private final NamespacedKey key;

    public AProperty(@NotNull String name,
                     @NotNull Class<E> entityClass,
                     @NotNull Class<S> valueClass,
                     @NotNull Function<E, S> getter,
                     @NotNull BiConsumer<E, S> setter,
                     @NotNull Supplier<S> defaultProvider) {
        this.key = craftKey(name);
        this.entityClass = entityClass;
        this.valueClass = valueClass;
        this.getter = getter;
        this.setter = setter;
        this.defaultProvider = defaultProvider;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static NamespacedKey craftKey(String name) {
        return new NamespacedKey("property", name.toLowerCase(Locale.ENGLISH));
    }

    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    @NotNull
    public String name() {
        return key.getKey();
    }

    @Override
    public @NotNull Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public @NotNull Class<S> getValueClass() {
        return valueClass;
    }

    public @NotNull Function<E, S> getGetter() {
        return getter;
    }

    public @NotNull BiConsumer<E, S> getSetter() {
        return setter;
    }

    public @NotNull Supplier<S> getDefaultProvider() {
        return defaultProvider;
    }

    @Override
    @Nullable
    public S getFromEntity(@NotNull E e) {
        return this.getter.apply(e);
    }

    @Override
    public void setToEntity(@NotNull E e, @Nullable S s) {
        this.setter.accept(e, s);
    }

    @Override
    @Nullable
    public S getDefault() {
        return defaultProvider.get();
    }

    @Override
    public void applyToEntity(@NotNull E e, @NotNull Map<String, Object> valueContainer, @Nullable Context context) {
        if (context != null && context.isPresent(this))
            setter.accept(e, context.getValue(this, e));
        else
            setter.accept(e, this.getFromMap(valueContainer));
    }

    @Override
    public void applyToMap(@NotNull E e, @NotNull Map<String, Object> valueContainer) {
        this.setToMap(valueContainer, this.getFromEntity(e));
    }

}

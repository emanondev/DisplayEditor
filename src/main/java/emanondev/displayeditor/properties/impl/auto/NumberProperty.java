package emanondev.displayeditor.properties.impl.auto;

import emanondev.displayeditor.YMLConfig;
import emanondev.displayeditor.properties.Context;
import emanondev.displayeditor.properties.Property;
import emanondev.displayeditor.properties.PropertyEditor;
import emanondev.displayeditor.utility.TriFunction;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

public class NumberProperty<E, V extends Number> implements Property<E, V> {
    private final String code;
    private final YMLConfig config;
    private final Class<E> entityClass;
    private final Class<V> valueClass;
    private final Method getter;
    private final Method setter;
    private final boolean isPrimitive;
    @Getter
    private final NamespacedKey key;
    private final TriFunction<YMLConfig, String, V, V> load;
    private final V absoluteMax;
    private final V absoluteMin;
    private final V absoluteDefault;

    public NumberProperty(@NotNull String code, @NotNull Class<E> entityClass, @NotNull Class<V> valueClass,
                          @NotNull Method getter, @NotNull Method setter, boolean isPrimitive,
                          @NotNull YMLConfig config, @NotNull TriFunction<YMLConfig, String, V, V> load,
                          @NotNull V absoluteMax, @NotNull V absoluteMin, @NotNull V absoluteDefault) {
        this.code = code;
        this.key = new NamespacedKey("property", code);
        this.entityClass = entityClass;
        this.valueClass = valueClass;
        this.getter = getter;
        this.setter = setter;
        this.isPrimitive = isPrimitive;
        this.config = config;
        this.load = load;
        this.absoluteMax = absoluteMax;
        this.absoluteMin = absoluteMin;
        this.absoluteDefault = absoluteDefault;
        load.apply(config, code + ".default_value", isPrimitive ? absoluteDefault : null);
        load.apply(config, code + ".min_value", absoluteMin);
        load.apply(config, code + ".max_value", absoluteMax);
        config.loadInteger(code + ".priority", 10);
        if (!isPrimitive) {
            config.loadBoolean(code + ".allow_null", true);
        }
    }

    @NotNull
    public V getMaxValue() {
        return load.apply(config, code + ".max_value", absoluteMax);
    }

    @NotNull
    public V getMinValue() {
        return load.apply(config, code + ".min_value", absoluteMin);
    }

    public boolean allowNull() {
        return !isPrimitive && config.getBoolean(code + ".allow_null", true);
    }

    @Nullable
    public V bound(@Nullable V value) {
        if (value == null && !allowNull()) {
            return getDefault();
        }
        if (value == null) {
            return null;
        }
        V min = getMinValue();
        V max = getMaxValue();

        BigDecimal v = new BigDecimal(value.toString());

        if (v.compareTo(new BigDecimal(min.toString())) < 0) return getMinValue();
        if (v.compareTo(new BigDecimal(max.toString())) > 0) return getMaxValue();
        return value;
    }


    @NotNull
    @Override
    public String name() {
        return code;
    }


    @Nullable
    @Override
    public V getFromEntity(@NotNull E e) {
        try {
            return (V) getter.invoke(e);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setToEntity(@NotNull E e, @Nullable V value) {
        try {
            Object arg = (isPrimitive && value == null) ? getDefault() : value;
            setter.invoke(e, arg);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nullable
    @Override
    public V getDefault() {
        return bound(load.apply(config, code + ".default_value", allowNull() ? null : absoluteDefault));
    }

    @Override
    public void applyToEntity(@NotNull E e, @NotNull Map<String, Object> valueContainer, @Nullable Context context) {
        try {
            setter.invoke(e, bound(getFromMap(valueContainer)));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void applyToMap(@NotNull E e, @NotNull Map<String, Object> valueContainer) {
        valueContainer.put(code, getFromEntity(e));
    }

    @Nullable
    @Override
    public V getFromMap(@NotNull Map<String, Object> map) {
        return bound((V) map.getOrDefault(code, getDefault()));
    }

    @Override
    public void setToMap(@NotNull Map<String, Object> map, @Nullable V integer) {
        map.put(code, integer);
    }

    @Override
    public PropertyEditor getPropertyEditor(E entity, Player player) {
        return null;
    }

    @Override
    public int getPriority() {
        return config.loadInteger(code + ".priority", 10);
    }

    @NotNull
    public Class<V> getValueClass(){
        return valueClass;
    }

    @NotNull
    public Class<E> getEntityClass(){
        return entityClass;
    }
}

package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.ConfSerCollProperty;
import emanondev.displayeditor.properties.impl.NumberProperty;
import emanondev.displayeditor.properties.impl.Property;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import java.util.*;

public class AttributeProperties {

    public static final Map<Attribute, Property<Attributable, Double>> ATTRIBUTABLE_BASE_VALUES;
    public static final Map<Attribute, Property<Attributable, Collection<AttributeModifier>>> ATTRIBUTABLE_MODIFIERS;

    static {
        Map<Attribute, Property<Attributable, Double>> baseValues = new LinkedHashMap<>();
        Map<Attribute, Property<Attributable, Collection<AttributeModifier>>> modifiers = new LinkedHashMap<>();
        for (Attribute attribute : Registry.ATTRIBUTE.stream().toList()) {
            baseValues.put(attribute, NumberProperty.fromDouble(
                    "ATTRIBUTABLE_" + attribute.getKey().getKey().toUpperCase(Locale.ENGLISH) + "_BASE_VALUE",
                    Attributable.class,
                    (Attributable att) -> {
                        AttributeInstance inst = att.getAttribute(attribute);
                        return inst == null ? null : inst.getBaseValue();
                    }, (Attributable att, Double value) -> {
                AttributeInstance inst = att.getAttribute(attribute);
                if (inst != null)
                    inst.setBaseValue(value);
            }, () -> 1D));

            modifiers.put(attribute, new ConfSerCollProperty<>(
                    "ATTRIBUTABLE_" + attribute.getKey().getKey().toUpperCase(Locale.ENGLISH) + "_MODIFIERS",
                    Attributable.class, (Class<Collection<AttributeModifier>>) (Class<?>) Collection.class,
                    AttributeModifier.class,
                    (Attributable att) -> {
                        AttributeInstance inst = att.getAttribute(attribute);
                        return inst == null ? null : inst.getModifiers();
                    }, (Attributable att, Collection<AttributeModifier> value) -> {
                AttributeInstance inst = att.getAttribute(attribute);
                if (inst != null && value != null) {
                    inst.getModifiers().forEach(inst::removeModifier);
                    value.forEach(inst::addModifier);
                }
            }, Collections::emptyList));
        }
        ATTRIBUTABLE_BASE_VALUES = Collections.unmodifiableMap(baseValues);
        ATTRIBUTABLE_MODIFIERS = Collections.unmodifiableMap(modifiers);
    }
}

package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.AttributeBaseValueProperty;
import emanondev.displayeditor.properties.impl.AttributeModifierProperty;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeProperties {

    public static final Map<Attribute, Property<Attributable, Double>> ATTRIBUTABLE_BASE_VALUES;
    public static final Map<Attribute, Property<Attributable, Collection<AttributeModifier>>> ATTRIBUTABLE_MODIFIERS;

    static {
        Map<Attribute, Property<Attributable, Double>> baseValues = new LinkedHashMap<>();
        Map<Attribute, Property<Attributable, Collection<AttributeModifier>>> modifiers = new LinkedHashMap<>();
        for (Attribute attribute : Registry.ATTRIBUTE.stream().toList()) {
            if (attribute.getKey().getKey().split("\\.")[0].equals("attributable_player"))
                continue;
            baseValues.put(attribute, new AttributeBaseValueProperty(attribute));
            modifiers.put(attribute, new AttributeModifierProperty(attribute));
        }
        ATTRIBUTABLE_BASE_VALUES = Collections.unmodifiableMap(baseValues);
        ATTRIBUTABLE_MODIFIERS = Collections.unmodifiableMap(modifiers);
    }
}

package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.PropertyEditor;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class AttributeModifierProperty extends ConfSerCollProperty<Attributable, AttributeModifier,
        Collection<AttributeModifier>> {

    private final Attribute attribute;

    public AttributeModifierProperty(@NotNull Attribute attribute) {
        super("ATTRIBUTABLE_" + attribute.getKey().getKey().toUpperCase(Locale.ENGLISH)
                        .replace(".", "_") + "_MODIFIERS"
                , Attributable.class, (Class<Collection<AttributeModifier>>) (Class<?>) Collection.class,
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
                }, Collections::emptyList);
        this.attribute = attribute;
    }

    @Override
    public PropertyEditor getPropertyEditor(Attributable entity, Player player) {
        if (entity.getAttribute(attribute) == null)
            return null;
        return super.getPropertyEditor(entity, player);
    }
}

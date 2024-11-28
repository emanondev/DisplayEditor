package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.PropertyEditor;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AttributeBaseValueProperty extends NumberProperty<Attributable, Double> {

    private final Attribute attribute;

    public AttributeBaseValueProperty(@NotNull Attribute attribute) {
        super("ATTRIBUTABLE_" + attribute.getKey().getKey().toUpperCase(Locale.ENGLISH)
                        .replace(".", "_") + "_BASE_VALUE"
                , Attributable.class, Double.class,
                (Attributable att) -> {
                    AttributeInstance inst = att.getAttribute(attribute);
                    return inst == null ? null : inst.getBaseValue();
                }, (Attributable att, Double value) -> {
                    AttributeInstance inst = att.getAttribute(attribute);
                    if (inst != null)
                        inst.setBaseValue(value);
                }, () -> 1D);
        this.attribute = attribute;
    }

    @Override
    public PropertyEditor getPropertyEditor(Attributable entity, Player player) {
        if (entity.getAttribute(attribute)==null)
            return null;
        return super.getPropertyEditor(entity,player);
    }
}

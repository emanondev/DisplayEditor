package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.Property;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("SerializedEntity")
public class SerializedEntity implements ConfigurationSerializable {

    public List<Property> properties;
    private final Map<String, Object> values;

    public SerializedEntity(Entity e) {
        List<Property<?, ?>> list = Registries.PROPERTIES.getAllByEntity(e.getClass());
        values = new LinkedHashMap<>();
        for (Property p : list) {
            p.applyToMap(e, values);
        }
    }

    public SerializedEntity(Map<String, Object> values) {
        this.values = new LinkedHashMap<>(values);
    }

    public void removeProperty(Property<?, ?> p) {
        if (!properties.contains(p))
            throw new IllegalArgumentException("Missing property " + p.getKey()
                    + " must be registered to Registries.PROPERTIES");
        p.setToMap(values, null);
    }

    public <S> void addProperty(Property<?, S> p, S value) {
        if (!properties.contains(p))
            throw new IllegalArgumentException("Missing property " + p.getKey()
                    + " must be registered to Registries.PROPERTIES");
        p.setToMap(values, value);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return new LinkedHashMap<>(values);
    }
}

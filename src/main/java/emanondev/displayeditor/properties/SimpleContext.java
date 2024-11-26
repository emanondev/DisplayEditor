package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.Property;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SimpleContext implements Context {

    private final Map<Property<?, ?>, Function<?, ?>> properties = new HashMap<>();

    @Override
    public <E, S> void register(@NotNull Property<E, S> property, @NotNull Function<E, S> provider) {
        properties.put(property, provider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E, S> S getValue(@NotNull Property<E, S> property, @NotNull E entity) {
        try {
            Function<E, S> provider = (Function<E, S>) properties.get(property);
            if (provider != null) {
                return provider.apply(entity);
            }
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        return property.getDefault();
    }

    @Override
    public <E, S> boolean isPresent(@NotNull Property<E, S> property) {
        return properties.containsKey(property);
    }
}

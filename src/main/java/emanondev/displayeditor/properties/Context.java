package emanondev.displayeditor.properties;

import emanondev.displayeditor.properties.impl.Property;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Context {

    <E, S> void register(@NotNull Property<E, S> property, @NotNull Function<E, S> provider);

    <E, S> S getValue(@NotNull Property<E, S> property, @NotNull E entity);

    <E, S> boolean isPresent(@NotNull Property<E, S> property);
}

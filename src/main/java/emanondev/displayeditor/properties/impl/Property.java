package emanondev.displayeditor.properties.impl;

import emanondev.displayeditor.properties.Context;
import io.lumine.mythic.bukkit.utils.functions.TriConsumer;
import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Property<E, S> extends Keyed {


    /*
    @SuppressWarnings("unchecked")
    public <D extends ConfigurationSerializable> Property(@NotNull String name, Class<E> entityClass,
                                                          @NotNull Class<Collection<D>> valueClass,
                                                          @NotNull Function<E, Collection<D>> getter,
                                                          @NotNull BiConsumer<E, Collection<D>> setter,
                                                          Function<Context, Collection<D>> defaultProvider) {
        this.key = craftKey(name);
        this.entityClass = entityClass;
        this.valueClass = (Class<S>) valueClass;
        this.getter = (Function<E, S>) getter;
        this.setter = (BiConsumer<E, S>) setter;
        this.toMap = (e, map) -> {
            Object value = getter.apply(e);
            map.put(name(), value);
        };
        this.mapToEntity = (e, map, c) -> {
            Object value = map.get(name());
            setter.accept(e, valueClass.isInstance(value) ? valueClass.cast(value) : defaultProvider.apply(c));
        };
        this.defaultProvider = (Function<Context, S>) defaultProvider;
    }


    @SuppressWarnings("unchecked")
    public <D extends ConfigurationSerializable> Property(@NotNull String name, Class<E> entityClass,
                                                          @NotNull Class<List<D>> valueClass,
                                                          @NotNull Function<E, List<D>> getter,
                                                          @NotNull BiConsumer<E, List<D>> setter,
                                                          Function<Context, List<D>> defaultProvider) {
        this.key = craftKey(name);
        this.entityClass = entityClass;
        this.valueClass = (Class<S>) valueClass;
        this.getter = (Function<E, S>) getter;
        this.setter = (BiConsumer<E, S>) setter;
        this.toMap = (e, map) -> {
            Object value = getter.apply(e);
            map.put(name(), value);
        };
        this.mapToEntity = (e, map, c) -> {
            Object value = map.get(name());
            setter.accept(e, valueClass.isInstance(value) ? valueClass.cast(value) : defaultProvider.apply(c));
        };
        this.defaultProvider = (Function<Context, S>) defaultProvider;
    }

    private <E, S extends ConfigurationSerializable> Property(
            @NotNull String name,
            @NotNull Class<E> entityClass,
            @NotNull Class<S> valueClass,
            @NotNull Supplier<S> defaultProvider,
            @NotNull Function<E, S> getter,
            @NotNull BiConsumer<E, S> setter) {
        return new Property<E, S>(
                name,
                entityClass,
                valueClass,
                getter,
                setter,
                defaultProvider,
                (e, map) -> map.put(name(), getter.apply(e)),
                (map) -> {
                    Object value = map.get(name());
                    setter.accept(e, valueClass.isInstance(value) ? valueClass.cast(value) : defaultProvider.apply(c));
                },
                mapToEntity
        );

        this.toMap = (e, map) ->

        {
            Object value = getter.apply(e);
            if (!Objects.equals(value, defaultProvider.apply(null)))
                map.put(name(), value);
        }

        ;
        this.mapToEntity = (e, map, c) ->

        {
            Object value = map.get(name());
            setter.accept(e, valueClass.isInstance(value) ? valueClass.cast(value) : defaultProvider.apply(c));
        }

        ;
        this.defaultProvider = (Function<Context, S>) defaultProvider;
    }

    public static <E, S extends ConfigurationSerializable> Property<E, S> fromConfigurationSerializable(
            @NotNull String name,
            @NotNull Class<E> entityClass,
            @NotNull Class<S> valueClass,
            @NotNull Supplier<S> defaultProvider,
            @NotNull Function<E, S> getter,
            @NotNull BiConsumer<E, S> setter) {
        return new Property<E, S>(
                name,
                entityClass,
                valueClass,
                getter,
                setter,
                defaultProvider,
                (e, map) -> {
                    map.put(craftName(name), getter.apply(e));
                },
                fromMap,
                mapToEntity
        );
    }*/

    @NotNull
    String name();

    @NotNull
    Class<E> getEntityClass();

    @NotNull
    Class<S> getValueClass();

    @NotNull
    Function<E, S> getGetter();

    @NotNull
    BiConsumer<E, S> getSetter();

    @NotNull
    Supplier<S> getDefaultProvider();

    @NotNull
    TriConsumer<E, Map<String, Object>, Context> getMapToEntity();

    @NotNull
    Function<Map<String, Object>, S> getFromMap();

    @NotNull
    BiConsumer<S, Map<String, Object>> getToMap();
}


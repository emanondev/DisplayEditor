package emanondev.displayeditor.properties.impl.auto;

import emanondev.displayeditor.DisplayEditor;
import emanondev.displayeditor.YMLConfig;
import emanondev.displayeditor.properties.Property;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Finder {

    public static Map<String, Property<?, ?>> propertiesByName;
    public static Map<Class<?>, SortedSet<Property<?, ?>>> propertiesByClass;

    public static SortedSet<Property<?, ?>> getProperties(Class<?> clazz) {
        if (!propertiesByClass.containsKey(clazz)) {
            scan(clazz);
        }
        return propertiesByClass.get(clazz);
    }

    private static void scan(Class<?> clazz) {
        TreeSet<Property<?, ?>> properties = new TreeSet<>();

        // Collect all interfaces recursively, including self if interface
        Set<Class<?>> interfaces = getAllInterfacesIncludingSelf(clazz);

        for (Class<?> iface : interfaces) {
            properties.addAll(extractPropertiesFromInterface(iface));
        }

        propertiesByClass.put(clazz, properties);
    }

    private static Set<Class<?>> getAllInterfacesIncludingSelf(Class<?> clazz) {
        Set<Class<?>> result = new LinkedHashSet<>();

        // If the class itself is an interface, include it
        if (clazz.isInterface()) {
            result.add(clazz);
        }

        // Walk the class hierarchy (in case clazz is a concrete class)
        while (clazz != null) {
            for (Class<?> iface : clazz.getInterfaces()) {
                collectInterfaceRecursively(iface, result);
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    private static void collectInterfaceRecursively(Class<?> iface, Set<Class<?>> result) {
        if (!result.add(iface)) return; // already visited

        for (Class<?> superIface : iface.getInterfaces()) {
            collectInterfaceRecursively(superIface, result);
        }
    }

    private static List<Property<?, ?>> extractPropertiesFromInterface(Class<?> iface) {

        Method[] methods = iface.getDeclaredMethods();
        String classPrefix = iface.getSimpleName().toLowerCase() + "_";
        List<Property<?, ?>> properties = new ArrayList<>();

        for (Method setter : methods) {
            if (!Modifier.isPublic(setter.getModifiers())) {
                continue;
            }
            String name = setter.getName();
            if (!name.startsWith("set") ||
                    setter.getReturnType() != void.class ||
                    setter.getParameterCount() != 1) {
                continue;
            }
            Class<?> paramType = setter.getParameterTypes()[0];
            String suffix = name.substring(3);
            String propertyName = toSnakeCase(decapitalize(suffix));
            String key = classPrefix + propertyName;
            if (propertiesByName.containsKey(key)) {
                properties.add(propertiesByName.get(key));
                continue;
            }

            Method getter = findGetter(iface, suffix, paramType);
            if (getter == null) {
                getter = findBooleanGetter(iface, suffix, paramType);
            }

            if (getter == null) {
                continue;
            }

            Property<?, ?> result = calculateProperty(key, iface, getter, setter, paramType);
            if (result != null) {
                propertiesByName.put(key, result);
                properties.add(result);
            }
        }
        return properties;
    }

    private static Property<?, ?> calculateProperty(String key, Class<?> iface, Method getter, Method setter, Class<?> paramType) {

        switch (paramType.getName()) {
            case "int", "java.lang.Integer" -> {
                return new NumberProperty<>(key, iface, Integer.class,
                        getter, setter, paramType.getName().equals("int"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        YMLConfig::loadInteger,
                        Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            }
            case "long", "java.lang.Long" -> {
                return new NumberProperty<>(key, iface, Long.class,
                        getter, setter, paramType.getName().equals("long"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        YMLConfig::loadLong,
                        Long.MIN_VALUE, Long.MAX_VALUE, 0L);
            }
            case "short", "java.lang.Short" -> {
                return new NumberProperty<>(key, iface, Short.class,
                        getter, setter, paramType.getName().equals("short"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        (c, p, v) -> {
                            Integer r =
                                    c.loadInteger(p, v == null ? null : v.intValue());
                            return r == null ? null : r.shortValue();
                        },
                        Short.MIN_VALUE, Short.MAX_VALUE, (short) 0);
            }
            case "byte", "java.lang.Byte" -> {
                return new NumberProperty<>(key, iface, Byte.class,
                        getter, setter, paramType.getName().equals("byte"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        (c, p, v) -> {
                            Integer r =
                                    c.loadInteger(p, v == null ? null : v.intValue());
                            return r == null ? null : r.byteValue();
                        },
                        Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 0);
            }
            case "float", "java.lang.Float" -> {
                return new NumberProperty<>(key, iface, Float.class,
                        getter, setter, paramType.getName().equals("float"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        (c, p, v) -> {
                            Double r =
                                    c.loadDouble(p, v == null ? null : v.doubleValue());
                            return r == null ? null : r.floatValue();
                        },
                        Float.MIN_VALUE, Float.MAX_VALUE, (float) 0);
            }
            case "double", "java.lang.Double" -> {
                return new NumberProperty<>(key, iface, Double.class,
                        getter, setter, paramType.getName().equals("double"),
                        DisplayEditor.get().getConfig("properties.yml"),
                        YMLConfig::loadDouble,
                        Double.MIN_VALUE, Double.MAX_VALUE, 0D);
            }
            case "boolean", "java.lang.Boolean" -> {
                // handle boolean / Boolean
            }

            case "java.lang.String" -> {
                // handle String
            }

            case "org.bukkit.inventory.ItemStack" -> {
                // handle String
            }
        }

        //TODO test namespacedkeys

        //TODO test enums


        return null;
    }

    private static Method findGetter(Class<?> iface, String suffix, Class<?> paramType) {
        try {
            Method getter = iface.getMethod("get" + suffix);
            if (getter.getParameterCount() == 0 && getter.getReturnType() == paramType)
                return getter;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private static Method findBooleanGetter(Class<?> iface, String suffix, Class<?> paramType) {
        try {
            Method getter = iface.getMethod("is" + suffix);
            if (getter.getParameterCount() == 0 &&
                    getter.getReturnType() == boolean.class &&
                    (paramType == boolean.class || paramType == Boolean.class))
                return getter;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    private static String decapitalize(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private static String toSnakeCase(String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}

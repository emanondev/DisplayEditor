package emanondev.displayeditor.properties;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@Getter
public class SmartProperty<T> {

    private final String code;
    private final Class<T> valueClass;
    private final Method getter;
    private final Method setter;

    public SmartProperty(@NotNull String code,
                         @NotNull Method getter,
                         @NotNull Method setter,
                         @NotNull Class<T> valueClass) {
        this.code = code;
        this.getter = getter;
        this.setter = setter;
        this.getter.setAccessible(true);
        this.setter.setAccessible(true);
        this.valueClass = valueClass;
    }

    public T get(Object object) throws Exception {
        return (T) getter.invoke(object);
    }

    public void set(Object object, T value) throws Exception {
        setter.invoke(object, value);
    }


}

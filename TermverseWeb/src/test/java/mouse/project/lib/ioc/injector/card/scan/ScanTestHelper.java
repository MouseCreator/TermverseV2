package mouse.project.lib.ioc.injector.card.scan;

import mouse.project.lib.ioc.injector.sources.annotation.Construct;
import mouse.project.lib.ioc.injector.sources.annotation.GetMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class ScanTestHelper {


    public <T> Constructor<T> getConstructor(Class<T> clazz, String key) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Construct annotation = constructor.getAnnotation(Construct.class);
            if (annotation == null) {
                continue;
            }
            if(annotation.key().equals(key)) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                try {
                    Constructor<T> result = clazz.getConstructor(parameterTypes);
                    result.setAccessible(true);
                    return result;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new NoSuchElementException("No constructor with key " + key + " for class " + clazz);
    }

    public Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod(Class<?> clazz, String key) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            GetMethod annotation = method.getAnnotation(GetMethod.class);
            if (annotation == null) {
                continue;
            }
            if(annotation.key().equals(key)) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new NoSuchElementException("No method with key " + key + " in class " + clazz);
    }
}

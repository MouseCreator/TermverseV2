package mouse.project.lib.ioc.injector;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.utils.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScanUtils {

    private ScanUtils() {
    }
    private static ScanUtils instance = null;
    public static ScanUtils getInstance() {
        if (instance == null) {
            instance = new ScanUtils();
        }
        return instance;
    }
    public <T> Optional<Constructor<T>> getNoArgsConstructor(Class<T> clazz) {
        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            return Optional.of(declaredConstructor);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    public <T> Constructor<T> toConstructor(Class<T> clazz, Constructor<?> constructor) {
        Constructor<T> result;
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        try {
            result = clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new CardException(e);
        }
        return result;
    }

     public <T> List<Method> getAnnotatedMethod(Class<T> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> result = new ArrayList<>();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(annotation)) {
                continue;
            }
            result.add(method);
        }
        return result;
    }

    public List<Field> getAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotation) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> result = new ArrayList<>();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(annotation)) {
                continue;
            }
            result.add(field);
        }
        return result;
    }

    public boolean isCollectionType(Class<?> type) {
        return TypeUtils.isCollectionType(type);
    }
}

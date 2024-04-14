package mouse.project.lib.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scanners {
    public static Collection<Method> getMethodsAnnotatedWith(Class<?> target, Class<? extends Annotation> annotation) {
        Method[] declaredMethods = target.getDeclaredMethods();
        List<Method> result = new ArrayList<>();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotation)) {
                result.add(method);
            }
        }
        return result;
    }
}

package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.exception.CollectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CollectionProducer {

    public Collection<?> create(Class<?> clazz, Collection<?> elements) {
        if (clazz.equals(Collection.class)) {
            return new ArrayList<>(elements);
        }
        if (List.class.isAssignableFrom(clazz)) {
            return createList(clazz, elements);
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return createSet(clazz, elements);
        }
        throw new CollectionException("Unsupported collection type: " + clazz);
    }

    private Collection<?> createSet(Class<?> clazz, Collection<?> implementations) {
        if (clazz.equals(Set.class)) {
            return new HashSet<>(implementations);
        }
        return createFromDefaultConstructorAndAdd(clazz, implementations);
    }

    private Collection<?> createList(Class<?> clazz, Collection<?> implementations) {
        if (clazz.equals(List.class)) {
            return new ArrayList<>(implementations);
        }
        return createFromDefaultConstructorAndAdd(clazz, implementations);
    }

    private Collection<?> createFromDefaultConstructorAndAdd(Class<?> clazz, Collection<?> implementations) {
        if (!clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
            try {
                Collection<?> result = (Collection<?>) clazz.getDeclaredConstructor().newInstance();
                addAllToResult(implementations, result);
                return result;
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("No default constructor available for " + clazz.getName(), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new CollectionException("Cannot instantiate interface or abstract class: " + clazz.getName());
    }

    private void addAllToResult(Collection<?> implementations, Collection<?> result)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = result.getClass().getMethod("addAll", Collection.class);
        method.setAccessible(true);
        method.invoke(result, implementations);
    }

    public Collection<?> create(Class<?> type) {
        return create(type, List.of());
    }
}

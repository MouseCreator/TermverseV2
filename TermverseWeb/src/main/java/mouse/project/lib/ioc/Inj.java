package mouse.project.lib.ioc;

import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.Collection;

public interface Inj {
    <T> T get(Class<T> clazz);
    <T> T get(Class<T> clazz, String name);
    <T> Collection<T> getAll(Class<T> clazz);
    Collection<Object> getFiltered(Collection<ImplementationFilter> filters);
    <T> Collection<T> getAll(Class<T> clazz, String name);
}

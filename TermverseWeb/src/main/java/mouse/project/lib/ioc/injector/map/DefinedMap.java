package mouse.project.lib.ioc.injector.map;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.Collection;

public interface DefinedMap<E extends TypeHolder<?>> {
    E lookup(Implementation<?> implementation);
    void add(E definition);
    Collection<E> lookupAll(Implementation<?> implementation);
    <T> boolean contains(Implementation<T> implementation);
    Collection<E> getFiltered(Collection<ImplementationFilter> filters);
}

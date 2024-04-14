package mouse.project.lib.ioc;

import mouse.project.lib.ioc.injector.card.Cards;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.Collection;
public class InjImpl implements Inj {
    private final Cards cards;
    public InjImpl(Cards cards) {
        this.cards = cards;
    }
    @Override
    public <T> T get(Class<T> clazz) {
        return cards.get(clazz);
    }

    @Override
    public <T> T get(Class<T> clazz, String name) {
        return cards.getNamed(clazz, name);
    }

    @Override
    public <T> Collection<T> getAll(Class<T> clazz) {
        return cards.getAll(clazz);
    }

    @Override
    public Collection<Object> getFiltered(Collection<ImplementationFilter> filters) {
        return cards.getAllAnnotatedWith(filters);
    }

    @Override
    public <T> Collection<T> getAll(Class<T> clazz, String name) {
        return cards.getAllNamed(clazz, name);
    }
}

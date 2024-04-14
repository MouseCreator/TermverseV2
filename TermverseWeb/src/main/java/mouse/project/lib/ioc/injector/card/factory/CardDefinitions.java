package mouse.project.lib.ioc.injector.card.factory;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.CardDefinition;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.Collection;

public interface CardDefinitions {
    CardDefinition<?> lookup(Implementation<?> implementation);
    void add(CardDefinition<?> definition);
    Collection<CardDefinition<?>> lookupAll(Implementation<?> implementation);
    Collection<CardDefinition<?>> lookupFiltered(Collection<ImplementationFilter> filters);
}

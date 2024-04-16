package mouse.project.lib.ioc.injector.card.factory;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.CardDefinition;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;
import mouse.project.lib.ioc.injector.map.DefinedMap;

import java.util.Collection;

public class CardDefinitionsImpl implements CardDefinitions {
    private final DefinedMap<CardDefinition<?>> map;
    public CardDefinitionsImpl(DefinedMap<CardDefinition<?>> map) {
        this.map = map;
    }
    @Override
    public CardDefinition<?> lookup(Implementation<?> implementation) {
        return map.lookup(implementation);
    }
    @Override
    public void add(CardDefinition<?> definition) {
        map.add(definition);
    }

    @Override
    public Collection<CardDefinition<?>> lookupAll(Implementation<?> implementation) {
        return map.lookupAll(implementation);
    }

    @Override
    public Collection<CardDefinition<?>> lookupFiltered(Collection<ImplementationFilter> filters) {
        return map.getFiltered(filters);
    }

}

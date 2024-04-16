package mouse.project.lib.ioc.injector.card.scan;

import mouse.project.lib.ioc.injector.card.definition.DefinedCard;
import mouse.project.lib.ioc.injector.card.definition.FactoryCard;
import mouse.project.lib.ioc.injector.card.factory.CardDefinitions;

import java.util.List;

public class DefinitionsManager {
    private final CardDefinitions definitions;

    public DefinitionsManager(CardDefinitions definitions) {
        this.definitions = definitions;
    }

    public void onAdd(DefinedCard<?> definedCard) {
        definitions.add(definedCard);
        List<FactoryCard<?>> factoryDefinitions = definedCard.getFactoryDefinitions();
        factoryDefinitions.forEach(definitions::add);
    }
}

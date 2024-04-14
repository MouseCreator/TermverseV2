package mouse.project.lib.ioc.injector.card.factory;

import lombok.Setter;
import mouse.project.lib.exception.CycleDependencyException;
import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.access.CardAccessImpl;
import mouse.project.lib.ioc.injector.card.container.CardContainer;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.CardDefinition;
import mouse.project.lib.ioc.injector.card.producer.CardProducer;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class CardFactoryImpl implements CardFactory {
    private CardContainer container;
    private CardDefinitions cardDefinitions;

    public CardFactoryImpl(CardContainer container, CardDefinitions cardDefinitions) {
        this.container = container;
        this.cardDefinitions = cardDefinitions;
    }

    public CardFactoryImpl() {
        container = null;
        cardDefinitions = null;
    }
    public <T> T buildCard(Implementation<T> implementation) {
        BuildStack buildStack = new BuildStack();
        return buildCard(implementation, buildStack);
    }

    @Override
    public <T> Collection<T> buildAllCards(Implementation<T> implementation) {
        BuildStack buildStack = new BuildStack();
        return buildAllCards(implementation, buildStack);
    }


    public <T> T buildCard(Implementation<T> current, BuildStack buildStack) {
        assertNotLopped(current, buildStack);
        CardDefinition<?> definition = cardDefinitions.lookup(current);
        if (definition.getType().isPrototype()) {
            return handlePrototype(current, definition, buildStack);
        } else {
            return handleSingleton(current, definition, buildStack);
        }

    }

    private static <T> void assertNotLopped(Implementation<T> current, BuildStack buildStack) {
        if (buildStack.contains(current)) {
            throw new CycleDependencyException("Cycle in card factory: " + buildStack + " looped back to " + current);
        }
    }

    @Override
    public <T> Collection<T> buildAllCards(Implementation<T> current, BuildStack buildStack) {
        Collection<CardDefinition<?>> definitions = cardDefinitions.lookupAll(current);
        List<T> implementations = new ArrayList<>();
        for (CardDefinition<?> definition : definitions) {
            Implementation<?> type = definition.getType();
            Object card = buildCard(type, buildStack);
            T cast = current.getClazz().cast(card);
            implementations.add(cast);
        }
        return implementations;
    }

    @Override
    public Collection<Object> getFiltered(Collection<ImplementationFilter> filters) {
        Collection<CardDefinition<?>> definitions = cardDefinitions.lookupFiltered(filters);
        List<Object> result = new ArrayList<>();
        for (CardDefinition<?> definition : definitions) {
            Implementation<?> type = definition.getType();
            Object obj = buildCard(type);
            result.add(obj);
        }
        return result;
    }


    private  <T> T handleSingleton(Implementation<T> current, CardDefinition<?> definition, BuildStack buildStack) {
        if (container.containsImplementation(current)) {
            return container.findImplementation(current);
        }
        Object obj = produceCard(current, definition, buildStack);
        container.put(obj);
        return current.getClazz().cast(obj);
    }

    private  <T> T handlePrototype(Implementation<T> current, CardDefinition<?> definition, BuildStack buildStack) {
        Object obj = produceCard(current, definition, buildStack);
        return current.getClazz().cast(obj);
    }

    private <T> Object produceCard(Implementation<T> current, CardDefinition<?> definition, BuildStack buildStack) {
        CardProducer<?> producer = definition.getProducer();
        BuildStack nextLevelBuildStack = buildStack.next(current);
        CardAccess cardAccess = new CardAccessImpl(nextLevelBuildStack, this);
        Object result = producer.produce(cardAccess);
        CardAccess newCardAccess = getNewCardAccess();
        producer.afterConstruction(result, newCardAccess);
        return result;
    }

    private CardAccess getNewCardAccess() {
        return new CardAccessImpl(new BuildStack(), this);
    }
}

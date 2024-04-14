package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.producer.CardProducer;

import java.util.List;

public class FactoryCardImpl<T> implements FactoryCard<T> {
    private final MethodDefinition factoryMethod;
    private final Implementation<T> type;

    public FactoryCardImpl(MethodDefinition factoryMethod, Implementation<T> type) {
        this.factoryMethod = factoryMethod;
        this.type = type;
    }

    @Override
    public CardProducer<T> getProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromFactory(this);
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return factoryMethod.requiredImplementations();
    }

    @Override
    public MethodDefinition getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public Implementation<T> getType() {
        return type;
    }
}

package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.ioc.injector.card.access.CardAccess;

import java.util.ArrayList;
import java.util.List;
public class DefinedProducerImpl<T> implements DefinedProducer<T> {
    private ConstructorProducer<T> constructorProducer;
    private final List<SetterProducer> setterProducerList;
    private final List<FieldProducer> fieldProducerList;
    private final List<ActionProducer> actionProducers;

    public DefinedProducerImpl() {
        constructorProducer = null;
        setterProducerList = new ArrayList<>();
        fieldProducerList = new ArrayList<>();
        actionProducers = new ArrayList<>();
    }

    @Override
    public T produce(CardAccess container) {
        if (constructorProducer == null) {
            throw new CardException("Producer is not defined");
        }
        T object = constructorProducer.construct(container);
        setterProducerList.forEach(sp -> sp.apply(object, container));
        fieldProducerList.forEach(sp -> sp.setField(object, container));
        return object;
    }

    @Override
    public void afterConstruction(Object constructed, CardAccess container) {
        actionProducers.forEach(a -> a.call(constructed, container));
    }

    @Override
    public void setConstructor(ConstructorProducer<T> constructor) {
        this.constructorProducer = constructor;
    }

    @Override
    public void addSetter(SetterProducer setterProducer) {
        this.setterProducerList.add(setterProducer);
    }

    @Override
    public void addFieldInjection(FieldProducer fieldProducer) {
        this.fieldProducerList.add(fieldProducer);
    }

    @Override
    public void addAction(ActionProducer actionProducer) {
        actionProducers.add(actionProducer);
    }
}

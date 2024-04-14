package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.ioc.injector.card.access.CardAccess;

public class FactoryMethodProducerImpl<T> implements FactoryMethodProducer<T> {
    private MethodProducer methodProducer;
    private final Class<T> clazz;

    public FactoryMethodProducerImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T produce(CardAccess container) {
        Object obj = methodProducer.call(container);
        try {
            return clazz.cast(obj);
        } catch (Exception e) {
            throw new CardException("Cannot cast to target class: " + clazz + " object " + obj);
        }
    }

    @Override
    public void afterConstruction(Object constructed, CardAccess container) {

    }

    @Override
    public void setMethod(MethodProducer method) {
        this.methodProducer = method;
    }
}

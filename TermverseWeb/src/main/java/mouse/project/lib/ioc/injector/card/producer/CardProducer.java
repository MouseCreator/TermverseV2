package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;

public interface CardProducer<T> {
    T produce(CardAccess container);
    void afterConstruction(Object constructed, CardAccess container);
}

package mouse.project.lib.ioc.injector.card.producer;


import mouse.project.lib.ioc.injector.card.access.CardAccess;

public interface ConstructorProducer<T> {
    T construct(CardAccess container);
}

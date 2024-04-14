package mouse.project.lib.ioc.injector.card.producer;


import mouse.project.lib.ioc.injector.card.access.CardAccess;

public interface MethodProducer {
    Object call(CardAccess container);
}

package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;

public interface SetterProducer {
    void apply(Object applyTo, CardAccess container);
}

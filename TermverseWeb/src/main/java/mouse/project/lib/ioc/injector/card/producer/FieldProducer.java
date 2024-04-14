package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;

public interface FieldProducer {
    void setField(Object obj, CardAccess container);
}

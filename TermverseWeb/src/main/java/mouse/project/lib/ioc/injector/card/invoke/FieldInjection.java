package mouse.project.lib.ioc.injector.card.invoke;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public interface FieldInjection {
    void setField(Object toChange, Object setTo);
    Implementation<?> getRequiredType();
}

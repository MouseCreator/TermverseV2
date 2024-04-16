package mouse.project.lib.ioc.injector.card.invoke;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.lang.reflect.Field;

public class FieldInjectionImpl implements FieldInjection {
    private final Field field;
    private final Implementation<?> implementation;

    public FieldInjectionImpl(Field field, Implementation<?> implementation) {
        this.field = field;
        this.implementation = implementation;
    }

    @Override
    public void setField(Object toChange, Object setTo) {
        field.setAccessible(true);
        try {
            field.set(toChange, setTo);
        } catch (IllegalAccessException e) {
            throw new CardException("Cannot set field " + field + " on object " + toChange + " to object " + setTo);
        }
    }

    @Override
    public Implementation<?> getRequiredType() {
        return implementation;
    }
}

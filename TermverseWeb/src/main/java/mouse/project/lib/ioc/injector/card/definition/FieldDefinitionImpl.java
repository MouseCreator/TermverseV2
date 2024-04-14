package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.producer.FieldProducer;

import java.lang.reflect.Field;
import java.util.List;

public class FieldDefinitionImpl implements FieldDefinition {
    private final Field field;
    public final Implementation<?> required;
    private final Class<?> collectionType;
    public FieldDefinitionImpl(Field field, Implementation<?> required, Class<?> collectionType) {
        this.field = field;
        this.required = required;
        this.collectionType = collectionType;
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return List.of(required);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public FieldProducer toProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromField(this);
    }

    @Override
    public Implementation<?> getType() {
        return required;
    }

    @Override
    public Class<?> collectionType() {
        return collectionType;
    }

    @Override
    public boolean isCollection() {
        return collectionType != null;
    }
}

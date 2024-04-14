package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.producer.FieldProducer;

import java.lang.reflect.Field;

public interface FieldDefinition extends DefinitionWithType, Collected {
    Field getField();
    FieldProducer toProducer();
}

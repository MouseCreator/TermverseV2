package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.producer.SetterProducer;

import java.lang.reflect.Method;

public interface SetterDefinition extends DefinitionWithParameters {
    Method getMethod();
    SetterProducer toProducer();
}

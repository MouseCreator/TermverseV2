package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.producer.ActionProducer;

import java.lang.reflect.Method;

public interface ActionDefinition extends DefinitionWithParameters {
    ActionProducer toProducer();
    Method getMethod();
}

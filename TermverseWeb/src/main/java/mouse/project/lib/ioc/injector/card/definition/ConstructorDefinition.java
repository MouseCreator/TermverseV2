package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.producer.ConstructorProducer;

import java.lang.reflect.Constructor;

public interface ConstructorDefinition<T> extends DefinitionWithParameters {
    ConstructorProducer<T> toProducer();
    Constructor<T> getConstructor();
}

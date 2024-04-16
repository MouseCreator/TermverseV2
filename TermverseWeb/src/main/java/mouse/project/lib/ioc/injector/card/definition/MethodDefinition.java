package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.producer.MethodProducer;

import java.lang.reflect.Method;

public interface MethodDefinition extends DefinitionWithParameters, DefinitionWithType {
    Method getMethod();
    MethodProducer toProducer();
    Implementation<?> getOrigin();
}

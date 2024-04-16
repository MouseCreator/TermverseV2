package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;
import mouse.project.lib.ioc.injector.card.producer.ConstructorProducer;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorDefinitionImpl<T> implements ConstructorDefinition<T> {
    private final Constructor<T> constructor;
    private final Parameters parameters;
    public ConstructorDefinitionImpl(Constructor<T> constructor, Parameters parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
    }
    @Override
    public ConstructorProducer<T> toProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromConstructor(this);
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return parameters.toRequirements();
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }
}

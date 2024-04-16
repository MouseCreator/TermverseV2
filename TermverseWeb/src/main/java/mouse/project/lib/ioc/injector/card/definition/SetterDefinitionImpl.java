package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;
import mouse.project.lib.ioc.injector.card.producer.SetterProducer;

import java.lang.reflect.Method;
import java.util.List;

public class SetterDefinitionImpl implements SetterDefinition {

    private final Method setter;
    private final Parameters parameters;

    public SetterDefinitionImpl(Method setter, Parameters parameters) {
        this.setter = setter;
        this.parameters = parameters;
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return parameters.toRequirements();
    }

    @Override
    public Method getMethod() {
        return setter;
    }

    @Override
    public SetterProducer toProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromSetter(this);
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }
}

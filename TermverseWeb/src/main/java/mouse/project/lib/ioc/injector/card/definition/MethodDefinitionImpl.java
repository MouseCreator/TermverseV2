package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;
import mouse.project.lib.ioc.injector.card.producer.MethodProducer;

import java.lang.reflect.Method;
import java.util.List;

public class MethodDefinitionImpl implements MethodDefinition {
    private final Method method;
    private final Implementation<?> returnType;
    private final Parameters methodParameters;
    private final Implementation<?> origin;

    public MethodDefinitionImpl(Method method,
                                Implementation<?> returnType,
                                Parameters methodParameters,
                                Implementation<?> origin) {
        this.method = method;
        this.returnType = returnType;
        this.methodParameters = methodParameters;
        this.origin = origin;
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return methodParameters.toRequirements();
    }

    @Override
    public Parameters getParameters() {
        return methodParameters;
    }

    @Override
    public Implementation<?> getType() {
        return returnType;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public MethodProducer toProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromMethod(this);
    }

    @Override
    public Implementation<?> getOrigin() {
        return origin;
    }
}

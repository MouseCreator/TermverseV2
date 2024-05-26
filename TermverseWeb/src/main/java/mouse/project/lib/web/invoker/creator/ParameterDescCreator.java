package mouse.project.lib.web.invoker.creator;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.DefaultValue;
import mouse.project.lib.web.annotation.Param;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.invoker.ArgumentSource;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;
import mouse.project.lib.web.invoker.desc.ParameterDescImpl;
import mouse.project.lib.web.invoker.processor.ParameterProcessor;

import java.lang.reflect.Parameter;
import java.util.Optional;

@Service
public class ParameterDescCreator implements DescCreator {

    private final ParameterProcessor parameterProcessor;
    @Auto
    public ParameterDescCreator(ParameterProcessor parameterProcessor) {
        this.parameterProcessor = parameterProcessor;
    }

    @Override
    public ArgumentDesc create(EndpointContext context, Parameter parameter) {
        Param annotation = parameter.getAnnotation(Param.class);
        String name = getName(parameter, annotation);
        String defaultValue = getDefault(parameter);
        Class<?> type = parameter.getType();
        return new ParameterDescImpl(type, name, defaultValue, parameterProcessor);
    }

    private String getDefault(Parameter parameter) {
        DefaultValue annotation = parameter.getAnnotation(DefaultValue.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }

    private static String getName(Parameter parameter, Param annotation) {
        String value = annotation.value();
        String name;
        if (value.isEmpty()) {
            name = parameter.getName();
        } else {
            name = value;
        }
        return name;
    }

    @Override
    public boolean accepts(ArgumentSource source) {
        return source == ArgumentSource.PARAMETER;
    }

    @Override
    public Optional<ArgumentSource> canProcess(Parameter parameter) {
        if (parameter.isAnnotationPresent(Param.class)) {
            return Optional.of(ArgumentSource.PARAMETER);
        }
        return Optional.empty();
    }
}

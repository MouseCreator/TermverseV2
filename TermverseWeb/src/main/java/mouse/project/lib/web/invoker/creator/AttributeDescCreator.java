package mouse.project.lib.web.invoker.creator;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.annotation.FromAttribute;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.invoker.ArgumentSource;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;
import mouse.project.lib.web.invoker.desc.AttributeDescImpl;

import java.lang.reflect.Parameter;
import java.util.Optional;

@Service
public class AttributeDescCreator implements DescCreator {
    @Override
    public ArgumentDesc create(EndpointContext context, Parameter parameter) {
        FromAttribute annotation = parameter.getAnnotation(FromAttribute.class);
        String attribute = annotation.value();
        Class<?> type = parameter.getType();
        return new AttributeDescImpl(attribute, type);
    }

    @Override
    public boolean accepts(ArgumentSource source) {
        return source == ArgumentSource.REQUEST_ATTRIBUTE;
    }

    @Override
    public Optional<ArgumentSource> canProcess(Parameter parameter) {
        if (parameter.isAnnotationPresent(FromAttribute.class)) {
            return Optional.of(ArgumentSource.REQUEST_ATTRIBUTE);
        }
        return Optional.empty();
    }
}

package mouse.project.lib.web.invoker.creator;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.utils.TypeUtils;
import mouse.project.lib.web.annotation.PType;
import mouse.project.lib.web.annotation.RBody;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.invoker.ArgumentSource;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;
import mouse.project.lib.web.invoker.desc.BodyDescImpl;
import mouse.project.lib.web.invoker.processor.BodyArgumentProcessor;

import java.lang.reflect.Parameter;
import java.util.Optional;

@Service
public class BodyArgumentDescCreator implements DescCreator{

    private final BodyArgumentProcessor processor;
    @Auto
    public BodyArgumentDescCreator(BodyArgumentProcessor processor) {
        this.processor = processor;
    }

    @Override
    public ArgumentDesc create(EndpointContext context, Parameter parameter) {
        RBody annotation = parameter.getAnnotation(RBody.class);
        String value = annotation.value();
        Class<?> type = parameter.getType();
        if (isCollectionType(type)) {
            PType pType = parameter.getAnnotation(PType.class);
            if (pType == null) {
                throw new ControllerException("Parameter " + parameter + " " +
                        "is collection type, but has no @PType annotation.");
            }
            Class<?> inner = pType.value();
            return new BodyDescImpl(type, inner, value, processor);
        } else {
            return new BodyDescImpl(type, value, processor);
        }
    }

    private boolean isCollectionType(Class<?> type) {
        return TypeUtils.isCollectionType(type);
    }

    @Override
    public boolean accepts(ArgumentSource source) {
        return source == ArgumentSource.BODY;
    }

    @Override
    public Optional<ArgumentSource> canProcess(Parameter parameter) {

        if (parameter.isAnnotationPresent(RBody.class)) {
            return Optional.of(ArgumentSource.BODY);
        }
        return Optional.empty();

    }
}

package mouse.project.lib.web.invoker.creator;

import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.invoker.ArgumentSource;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;

import java.lang.reflect.Parameter;
import java.util.Optional;

public interface DescCreator {
    ArgumentDesc create(EndpointContext context, Parameter parameter);
    boolean accepts(ArgumentSource source);
    Optional<ArgumentSource> canProcess(Parameter parameter);
}

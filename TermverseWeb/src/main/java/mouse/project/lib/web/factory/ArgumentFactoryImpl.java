package mouse.project.lib.web.factory;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.context.EndpointContext;
import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.invoker.ArgumentSource;
import mouse.project.lib.web.invoker.creator.DescCreator;
import mouse.project.lib.web.invoker.desc.ArgumentDesc;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

@Service
public class ArgumentFactoryImpl implements ArgumentFactory {
    private final List<DescCreator> descCreatorList;
    @Auto
    public ArgumentFactoryImpl(@Collect(DescCreator.class) List<DescCreator> descCreatorList) {
        this.descCreatorList = descCreatorList;
    }

    @Override
    public ArgumentDesc createArgument(EndpointContext context, Parameter parameter) {
        ArgumentSource source = getSource(parameter);
        for (DescCreator descCreator : descCreatorList) {
            if (descCreator.accepts(source)) {
                return descCreator.create(context, parameter);
            }
        }
        throw new ControllerException("No description creator for source: " + source);
    }

    private ArgumentSource getSource(Parameter parameter) {
        for (DescCreator descCreator : descCreatorList) {
            Optional<ArgumentSource> argumentSource = descCreator.canProcess(parameter);
            if (argumentSource.isPresent()) {
                return argumentSource.get();
            }
        }
        throw new ControllerException("Unknown source for parameter: " + parameter);
    }
}

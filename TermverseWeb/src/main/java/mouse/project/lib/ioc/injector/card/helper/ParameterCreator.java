package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.ParameterDefinition;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParameterCreator {

    private final CardAccess access;

    public ParameterCreator(CardAccess access) {
        this.access = access;
    }

    public Object assign(ParameterDefinition parameter) {
        Implementation<?> type = parameter.type();
        if(parameter.isCollection()) {
            CollectionProducer producer = new CollectionProducer();
            Collection<?> allImplementations = access.getAllImplementations(type);
            return producer.create(parameter.collectionType(), allImplementations);
        } else {
            return access.getImplementation(type);
        }
    }

    public List<Object> assignAll(Parameters parameters) {
        List<ParameterDefinition> parameterDefinitions = parameters.getParameterDefinitions();
        List<Object> args = new ArrayList<>();
        for (ParameterDefinition param : parameterDefinitions) {
            Object implementation = assign(param);
            args.add(implementation);
        }
        return args;
    }
}

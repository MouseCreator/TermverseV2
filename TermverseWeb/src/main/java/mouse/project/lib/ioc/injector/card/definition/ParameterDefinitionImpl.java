package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public record ParameterDefinitionImpl(Implementation<?> type, int order, Class<?> collectionType)
        implements ParameterDefinition {

    @Override
    public boolean isCollection() {
        return collectionType!=null;
    }
}

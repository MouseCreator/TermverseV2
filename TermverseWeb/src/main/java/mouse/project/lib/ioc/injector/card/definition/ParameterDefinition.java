package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public interface ParameterDefinition extends Collected {
    int order();
    Implementation<?> type();

}

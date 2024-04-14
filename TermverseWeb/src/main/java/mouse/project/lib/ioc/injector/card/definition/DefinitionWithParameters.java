package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.invoke.Parameters;

public interface DefinitionWithParameters extends Definition{
    Parameters getParameters();
}

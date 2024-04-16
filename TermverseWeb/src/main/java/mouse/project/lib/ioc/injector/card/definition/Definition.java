package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.util.List;

public interface Definition {
    List<Implementation<?>> requiredImplementations();
}

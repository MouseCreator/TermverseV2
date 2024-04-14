package mouse.project.lib.ioc.injector.card.requirement;

import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.util.Collection;
import java.util.List;

public interface RequirementSet {
    List<Implementation<?>> getRequirements();
    void addAll(Collection<Implementation<?>> requirements);
    void add(Implementation<?> requirement);
}

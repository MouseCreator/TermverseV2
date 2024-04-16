package mouse.project.lib.ioc.injector.card.requirement;

import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.util.*;

public class RequirementSetImpl implements RequirementSet {

    private final Set<Implementation<?>> set;

    public RequirementSetImpl() {
        set = new HashSet<>();
    }

    @Override
    public List<Implementation<?>> getRequirements() {
        return new ArrayList<>(set);
    }

    @Override
    public void addAll(Collection<Implementation<?>> requirements) {
        set.addAll(requirements);
    }

    @Override
    public void add(Implementation<?> requirement) {
        set.add(requirement);
    }
}

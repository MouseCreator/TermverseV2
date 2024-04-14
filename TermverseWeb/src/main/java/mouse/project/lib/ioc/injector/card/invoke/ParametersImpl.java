package mouse.project.lib.ioc.injector.card.invoke;

import lombok.NonNull;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.ParameterDefinition;

import java.util.*;

public class ParametersImpl implements Parameters {

    private final List<ParameterDefinition> parameterDefinitions;

    public ParametersImpl() {
        parameterDefinitions = new ArrayList<>();
    }

    public void add(ParameterDefinition parameterDefinition) {
        parameterDefinitions.add(parameterDefinition);
    }

    @Override
    public List<ParameterDefinition> getParameterDefinitions() {
        return parameterDefinitions
                .stream()
                .sorted(Comparator.comparingInt(ParameterDefinition::order))
                .toList();
    }

    @Override
    public int size() {
        return parameterDefinitions.size();
    }

    @Override
    public List<Implementation<?>> toRequirements() {
        Set<Implementation<?>> implementationSet = new HashSet<>();
        for (ParameterDefinition definition : parameterDefinitions) {
            Implementation<?> type = definition.type();
            implementationSet.add(type);
        }
        return new ArrayList<>(implementationSet);
    }

    @Override
    @NonNull
    public Iterator<ParameterDefinition> iterator() {
        return getParameterDefinitions().iterator();
    }
}

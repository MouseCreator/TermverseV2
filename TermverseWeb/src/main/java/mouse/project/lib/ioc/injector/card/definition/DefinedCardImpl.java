package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.ioc.injector.card.builder.Builder;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.producer.CardProducer;
import mouse.project.lib.ioc.injector.card.requirement.RequirementSet;
import mouse.project.lib.ioc.injector.card.requirement.RequirementSetImpl;

import java.util.ArrayList;
import java.util.List;

public class DefinedCardImpl<T> implements DefinedCard<T> {
    private ConstructorDefinition<T> primaryConstructor;
    private final List<SetterDefinition> setterDefinitions;
    private final List<FieldDefinition> fieldDefinitions;
    private final Implementation<T> origin;
    private final RequirementSet requirementSet;
    private final List<FactoryCard<?>> factoryFor;
    private final List<ActionDefinition> actionDefinitions;
    public DefinedCardImpl(Implementation<T> origin) {
        this.origin = origin;
        requirementSet = new RequirementSetImpl();
        primaryConstructor = null;
        setterDefinitions = new ArrayList<>();
        fieldDefinitions = new ArrayList<>();
        factoryFor = new ArrayList<>();
        actionDefinitions = new ArrayList<>();
    }

    @Override
    public ConstructorDefinition<T> getConstructor() {
        return primaryConstructor;
    }

    @Override
    public List<SetterDefinition> getSetters() {
        return new ArrayList<>(setterDefinitions);
    }

    @Override
    public List<FieldDefinition> getFields() {
        return new ArrayList<>(fieldDefinitions);
    }

    @Override
    public List<ActionDefinition> getActions() {
        return new ArrayList<>(actionDefinitions);
    }

    @Override
    public void addSetter(SetterDefinition setterDefinition) {
        requirementSet.addAll(setterDefinition.requiredImplementations());
        setterDefinitions.add(setterDefinition);
    }

    @Override
    public void addField(FieldDefinition fieldDefinition) {
        requirementSet.add(fieldDefinition.getType());
        fieldDefinitions.add(fieldDefinition);
    }

    @Override
    public void setPrimaryConstructor(ConstructorDefinition<T> constructor) {
        if (primaryConstructor != null) {
            throw new CardException("Primary constructor is already defined for " + origin);
        }
        this.primaryConstructor = constructor;
        requirementSet.addAll(constructor.requiredImplementations());
    }

    @Override
    public void addFactoryDefinition(FactoryCard<?> factoryCard) {
        factoryFor.add(factoryCard);
    }


    @Override
    public List<FactoryCard<?>> getFactoryDefinitions() {
        return new ArrayList<>(factoryFor);
    }

    @Override
    public void addAction(ActionDefinition actionDefinition) {
        actionDefinitions.add(actionDefinition);
    }

    @Override
    public CardProducer<T> getProducer() {
        Builder builder = Builder.getInstance();
        return builder.fromCard(this);
    }

    @Override
    public Implementation<T> getType() {
        return origin;
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return requirementSet.getRequirements();
    }
}

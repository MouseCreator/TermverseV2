package mouse.project.lib.ioc.injector.card.definition;

import java.util.List;

public interface DefinedCard<T> extends CardDefinition<T> {
    ConstructorDefinition<T> getConstructor();
    List<SetterDefinition> getSetters();
    List<FieldDefinition> getFields();
    List<ActionDefinition> getActions();
    void addSetter(SetterDefinition setterDefinition);
    void addField(FieldDefinition fieldDefinition);
    void setPrimaryConstructor(ConstructorDefinition<T> constructor);
    void addFactoryDefinition(FactoryCard<?> factoryCard);
    List<FactoryCard<?>> getFactoryDefinitions();
    void addAction(ActionDefinition actionDefinition);
}

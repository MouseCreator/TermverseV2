package mouse.project.lib.ioc.injector.card.builder;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.*;
import mouse.project.lib.ioc.injector.card.invoke.*;
import mouse.project.lib.ioc.injector.card.producer.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class Builder {
    private static Builder instance = null;

    public static Builder getInstance() {
        if (instance == null) {
            instance = new Builder();
        }
        return instance;
    }

    private Builder() {
    }

    public <T> ConstructorProducer<T> fromConstructor(ConstructorDefinition<T> definition) {
        Constructor<T> constructor = definition.getConstructor();
        Parameters parameters = definition.getParameters();
        ConstructorInvoker<T> constructorInvoker = new ConstructorInvokerImpl<>(constructor, parameters);
        return new ConstructorProducerImpl<>(constructorInvoker);
    }

    public SetterProducer fromSetter(SetterDefinition definition) {
        Method method = definition.getMethod();
        Parameters parameters = definition.getParameters();
        MethodInvoker methodInvoker = new MethodInvokerImpl(method, parameters);
        return new SetterProducerImpl(methodInvoker);
    }

    public FieldProducer fromField(FieldDefinition definition) {
        Field field = definition.getField();
        Implementation<?> implementation = definition.getType();
        FieldInjection fieldInjection = new FieldInjectionImpl(field, implementation);
        if (definition.isCollection()) {
            return new FieldCollectionProducer(fieldInjection, definition.collectionType());
        } else {
            return new FieldProducerImpl(fieldInjection);
        }

    }

    public <T> CardProducer<T> fromCard(DefinedCard<T> cardDefinition) {
        ConstructorDefinition<T> constructor = cardDefinition.getConstructor();
        DefinedProducer<T> definedProducer = new DefinedProducerImpl<>();
        definedProducer.setConstructor(fromConstructor(constructor));

        List<SetterDefinition> setters = cardDefinition.getSetters();
        setters.forEach(s -> definedProducer.addSetter(s.toProducer()));
        List<FieldDefinition> fields = cardDefinition.getFields();
        fields.forEach(f -> definedProducer.addFieldInjection(f.toProducer()));
        List<ActionDefinition> actionDefinitions = cardDefinition.getActions();
        actionDefinitions.forEach(a -> definedProducer.addAction(a.toProducer()));

        return definedProducer;
    }

    public MethodProducer fromMethod(MethodDefinition methodDefinition) {
        Method method = methodDefinition.getMethod();
        Parameters parameters = methodDefinition.getParameters();
        Implementation<?> origin = methodDefinition.getOrigin();
        MethodInvoker methodInvoker = new MethodInvokerImpl(method, parameters, origin);
        return new MethodProducerImpl(methodInvoker);
    }

    public <T> CardProducer<T> fromFactory(FactoryCard<T> factoryCard) {
        MethodDefinition factoryMethod = factoryCard.getFactoryMethod();
        Implementation<T> type = factoryCard.getType();
        FactoryMethodProducerImpl<T> factoryMethodProducer = new FactoryMethodProducerImpl<>(type.getClazz());
        factoryMethodProducer.setMethod(factoryMethod.toProducer());
        return factoryMethodProducer;
    }

    public ActionProducer fromAction(ActionDefinitionImpl actionDefinition) {
        Method method = actionDefinition.getMethod();
        Parameters parameters = actionDefinition.getParameters();
        ActionInvoker actionInvoker = new ActionInvokerImpl(method, parameters);
        return new ActionProducerImpl(actionInvoker);
    }
}

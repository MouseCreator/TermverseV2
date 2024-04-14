package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.*;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefinitionHelperImpl implements DefinitionHelper {
    private final DefinitionsInspector definitionsInspector;

    public DefinitionHelperImpl() {
        definitionsInspector = new DefinitionsInspector();
    }

    @Override
    public <T> ConstructorDefinition<T> getConstructor(Constructor<T> constructor) {
        Parameters parameters = definitionsInspector.inspectConstructor(constructor);
        return new ConstructorDefinitionImpl<>(constructor, parameters);
    }
    @Override
    public FieldDefinition getField(Field field) {
        FieldInfo info = definitionsInspector.inspectField(field);
        return new FieldDefinitionImpl(field, info.implementation(), info.collectionType());
    }

    @Override
    public SetterDefinition getSetter(Method method) {
        Parameters parameters = definitionsInspector.inspectMethod(method);
        return new SetterDefinitionImpl(method, parameters);
    }

    @Override
    public MethodDefinition getFactoryMethod(Method method, Implementation<?> origin) {
        Parameters parameters = definitionsInspector.inspectMethod(method);
        Implementation<?> returnType = definitionsInspector.getMethodReturnType(method);
        return new MethodDefinitionImpl(method, returnType, parameters, origin);
    }

    @Override
    public ActionDefinition getAction(Method method) {
        Parameters parameters = definitionsInspector.inspectMethod(method);
        return new ActionDefinitionImpl(method, parameters);
    }
}

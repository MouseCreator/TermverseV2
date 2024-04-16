package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface DefinitionHelper {
    <T> ConstructorDefinition<T> getConstructor(Constructor<T> constructor);
    FieldDefinition getField(Field field);
    SetterDefinition getSetter(Method method);
    MethodDefinition getFactoryMethod(Method method, Implementation<?> origin);
    ActionDefinition getAction(Method method);
}

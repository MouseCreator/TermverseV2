package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.exception.MissingAnnotationException;
import mouse.project.lib.ioc.annotation.Collect;
import mouse.project.lib.ioc.annotation.Name;
import mouse.project.lib.ioc.annotation.UseNamed;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.ParameterDefinition;
import mouse.project.lib.ioc.injector.card.definition.ParameterDefinitionImpl;
import mouse.project.lib.ioc.injector.card.invoke.Parameters;
import mouse.project.lib.ioc.injector.card.invoke.ParametersImpl;
import mouse.project.lib.utils.TypeUtils;

import java.lang.reflect.*;

public class DefinitionsInspector {

    public FieldInfo inspectField(Field field) {
        Class<?> requiredType = field.getType();
        UseNamed useNamed = field.getAnnotation(UseNamed.class);
        String named = useNamed == null ? null : useNamed.name();
        if (isCollection(field)) {
            return inspectCollectionField(named, field);
        }
        return new FieldInfo(new Implementation<>(requiredType, named), null);
    }

    public ParameterDefinition inspectParameter(Parameter parameter, int order, String metadata) {
        UseNamed annotation = parameter.getAnnotation(UseNamed.class);
        String named = annotation == null ? null : annotation.name();
        Class<?> requiredType = parameter.getType();
        if (isCollection(parameter)) {
            return inspectCollectionParameter(named, parameter, order, metadata);
        } else {
            Implementation<?> implementation = new Implementation<>(requiredType, named);
            return new ParameterDefinitionImpl(implementation, order, null);
        }
    }
    private FieldInfo inspectCollectionField(String named, Field field) {
        Class<?> collection = field.getType();
        Implementation<?> implementation = toCollectedImplementation(named, field);
        return new FieldInfo(implementation, collection);
    }
    private Implementation<?> toCollectedImplementation(String named, AnnotatedElement el, String metadata) {
        Collect collect = el.getAnnotation(Collect.class);
        if (collect == null) {
            String message = "Collection field/parameter must be annotated with @Collect " +
                    "and specify collection generic type " + el;
            if (metadata != null && !metadata.isEmpty()) {
                message += "Details: ";
                message += metadata;
            }
            throw new MissingAnnotationException(message);
        }
        Class<?> type = collect.value();
        return new Implementation<>(type, named);
    }
    private Implementation<?> toCollectedImplementation(String named, AnnotatedElement el) {
       return toCollectedImplementation(named, el, "");
    }


    private ParameterDefinition inspectCollectionParameter(String named, Parameter parameter, int order, String metadata) {
        Class<?> collection = parameter.getType();
        Implementation<?> implementation = toCollectedImplementation(named, parameter, metadata);
        return new ParameterDefinitionImpl(implementation, order, collection);
    }

    private boolean isCollection(Field field) {
        return isCollection(field.getType());
    }
    private boolean isCollection(Parameter parameter) {
        return isCollection(parameter.getType());
    }
    private boolean isCollection(Class<?> type) {
        return TypeUtils.isCollectionType(type);
    }

    public Parameters inspectParameters(Parameter[] parameters, String metadata) {
        ParametersImpl result = new ParametersImpl();
        int order = 0;
        for (Parameter parameter : parameters) {
            ParameterDefinition pd = inspectParameter(parameter, order, metadata);
            result.add(pd);
            order++;
        }
        return result;
    }

    public Parameters inspectConstructor(Constructor<?> constructor) {
        return inspectParameters(constructor.getParameters(), constructor.toString());
    }

    public Parameters inspectMethod(Method method) {
        return inspectParameters(method.getParameters(), method.toString());
    }

    public Implementation<?> getMethodReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Name annotation = method.getAnnotation(Name.class);
        String name = annotation == null ? null : annotation.name();
        if (isCollection(returnType)) {
            return toCollectedImplementation(name, method);
        }
        return new Implementation<>(returnType, name);
    }


}

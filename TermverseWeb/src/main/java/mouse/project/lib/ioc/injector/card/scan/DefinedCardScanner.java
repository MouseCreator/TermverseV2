package mouse.project.lib.ioc.injector.card.scan;

import mouse.project.lib.exception.CardException;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Factory;
import mouse.project.lib.ioc.injector.TypeValidator;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.container.Implementations;
import mouse.project.lib.ioc.injector.card.definition.*;
import mouse.project.lib.ioc.injector.card.helper.DefinitionHelper;
import mouse.project.lib.ioc.injector.card.helper.DefinitionHelperImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mouse.project.lib.ioc.injector.Singletons.scanUtils;

public class DefinedCardScanner implements CardScanner {

    private final ConstructorScanner constructorScanner;
    private final List<GenericScanner> genericScanners = new ArrayList<>();
    public DefinedCardScanner() {
        DefinitionHelper definitionHelper = new DefinitionHelperImpl();
        constructorScanner = new ConstructorScanner(definitionHelper);
        genericScanners.add(new MethodScanner(definitionHelper));
        genericScanners.add(new FieldScanner(definitionHelper));
        genericScanners.add(new FactoryScanner(definitionHelper));
        genericScanners.add(new ActionScanner(definitionHelper));
    }
    @Override
    public <T> DefinedCard<T> scan(Class<T> tClass) {
        TypeValidator typeValidator = new TypeValidator();
        typeValidator.validateCanBeProduced(tClass);
        Implementation<T> implementation = Implementations.create(tClass);
        DefinedCard<T> cardDefinition = new DefinedCardImpl<>(implementation);
        scanGivenClass(cardDefinition, tClass);
        scanSuperClasses(cardDefinition, tClass);
        return cardDefinition;
    }

    private <T> void scanGivenClass(DefinedCard<T> cardDefinition, Class<T> tClass) {
        constructorScanner.scan(cardDefinition, tClass);
        genericScanners.forEach(s -> s.scan(cardDefinition, tClass));
    }

    private <T> void scanSuperClasses(DefinedCard<T> definedCard, Class<?> tClass) {
        while (tClass != Object.class) {
            tClass = tClass.getSuperclass();
            final Class<?> current = tClass;
            genericScanners.forEach(s -> s.scan(definedCard, current));
        }
    }
    private record ConstructorScanner(DefinitionHelper definitionHelper) {
        public <T> void scan(DefinedCard<T> card, Class<T> toScan) {
            Optional<Constructor<T>> annotated = getAnnotatedConstructor(toScan);
            if (annotated.isPresent()) {
                Constructor<T> classConstructor = annotated.get();
                ConstructorDefinition<T> constructor = definitionHelper.getConstructor(classConstructor);
                card.setPrimaryConstructor(constructor);
                return;
            }

            Optional<Constructor<T>> singleConstructor = getSingleConstructor(toScan);
            if (singleConstructor.isPresent()) {
                ConstructorDefinition<T> constructor = definitionHelper.getConstructor(singleConstructor.get());
                card.setPrimaryConstructor(constructor);
                return;
            }

            throw new CardException("Neither @Auto, nor single constructor found for class: " + toScan);
        }

        private <T> Optional<Constructor<T>> getSingleConstructor(Class<T> clazz) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors.length == 1) {
                return Optional.of(scanUtils().toConstructor(clazz, constructors[0]));
            }
            return Optional.empty();
        }

        private <T> Optional<Constructor<T>> getAnnotatedConstructor(Class<T> clazz) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<T> result = null;
            for (Constructor<?> constructor : constructors) {
                Auto annotation = constructor.getAnnotation(Auto.class);
                if (annotation == null) {
                    continue;
                }
                if (result != null) {
                    throw new CardException("More than one @Auto-constructor found for: " + clazz);
                }
                result = scanUtils().toConstructor(clazz, constructor);
            }
            return Optional.ofNullable(result);
        }


    }
    private interface GenericScanner {
        <T> void scan(DefinedCard<T> card, Class<?> toScan);
    }
    private record FieldScanner(DefinitionHelper definitionHelper) implements GenericScanner {

        public <T> void scan(DefinedCard<T> card, Class<?> toScan) {
            List<Field> annotatedFields = scanUtils().getAnnotatedFields(toScan, Auto.class);
            for (Field field : annotatedFields) {
                FieldDefinition fieldDef = definitionHelper.getField(field);
                card.addField(fieldDef);
            }
        }
    }

    private record MethodScanner(DefinitionHelper definitionHelper) implements GenericScanner {

        public <T> void scan(DefinedCard<T> card, Class<?> toScan) {
            List<Method> methods = getAnnotatedSetters(toScan);
            for (Method method : methods) {
                SetterDefinition setter = definitionHelper.getSetter(method);
                card.addSetter(setter);
            }
        }
        private <T> List<Method> getAnnotatedSetters(Class<T> clazz) {
            return scanUtils().getAnnotatedMethod(clazz, Auto.class);
        }
    }

    private record ActionScanner(DefinitionHelper definitionHelper) implements GenericScanner {

        public <T> void scan(DefinedCard<T> card, Class<?> toScan) {
            List<Method> methods = getActions(toScan);
            for (Method method : methods) {
                ActionDefinition actionDefinition = definitionHelper.getAction(method);
                card.addAction(actionDefinition);
            }
        }

        private <T> List<Method> getActions(Class<T> clazz) {
            return scanUtils().getAnnotatedMethod(clazz, After.class);
        }
    }

    private record FactoryScanner(DefinitionHelper definitionHelper) implements GenericScanner {

        public <T> void scan(DefinedCard<T> card, Class<?> toScan) {
            List<Method> methods = getFactories(toScan);
            for (Method method : methods) {
                MethodDefinition factoryMethod = definitionHelper.getFactoryMethod(method, card.getType());
                card.addFactoryDefinition(new FactoryCardImpl<>(factoryMethod, Implementations.create(method)));
            }
        }

        private <T> List<Method> getFactories(Class<T> clazz) {
            return scanUtils().getAnnotatedMethod(clazz, Factory.class);
        }
    }
}

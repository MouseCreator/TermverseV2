package mouse.project.lib.ioc.injector;

import mouse.project.lib.exception.TypeException;

import java.lang.reflect.Modifier;

public class TypeValidator {
    public void validateCanBeProduced(Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new TypeException("Cannot produce an interface: " + clazz);
        }
        if(Modifier.isAbstract(clazz.getModifiers())) {
            throw new TypeException("Cannot produce an abstract class: " + clazz);
        }
        if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            throw new TypeException("Cannot produce inner non-static class:" + clazz);
        }
    }

    public boolean canBeProduced(Class<?> origin) {
        try {
            validateCanBeProduced(origin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

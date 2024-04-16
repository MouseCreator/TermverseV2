package mouse.project.lib.data.orm.fill;

import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.ioc.annotation.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
@Service
public class FillUtilService {
    public <T> T construct(Constructor<T> constructor) {
        if (constructor == null) {
            throw new ORMException("No constructor defined for model!");
        }
        if (constructor.getParameterCount() > 0) {
            throw new ORMException("Constructor " + constructor + " is not default constructor");
        }
        constructor.setAccessible(true);
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ORMException("Unable to construct model", e);
        }
    }

    public void assign(Object obj, Object value, Field field) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new ORMException("Unable to assign value", e);
        }
    }
}

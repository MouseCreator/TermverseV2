package mouse.project.lib.data.orm.desc;

import java.lang.reflect.Constructor;
import java.util.List;

public interface ModelDescription<T> {
    FieldDescriptions getFields();
    Class<T> forClass();
    Constructor<T> getConstructor();
}

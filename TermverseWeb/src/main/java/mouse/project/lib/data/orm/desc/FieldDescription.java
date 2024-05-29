package mouse.project.lib.data.orm.desc;

import java.lang.reflect.Field;

public interface FieldDescription {
    String columnName();
    Field field();
    Class<?> requiredClass();
    FieldDescType type();
}

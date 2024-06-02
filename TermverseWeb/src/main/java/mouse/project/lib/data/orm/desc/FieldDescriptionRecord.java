package mouse.project.lib.data.orm.desc;

import java.lang.reflect.Field;

public record FieldDescriptionRecord(String columnName,
                                     Field field,
                                     Class<?> requiredClass,
                                     FieldDescType type) implements FieldDescription {

}

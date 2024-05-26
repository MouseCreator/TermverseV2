package mouse.project.lib.data.orm.scan;

import jakarta.persistence.Column;
import mouse.project.lib.data.exception.ModelScanException;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.lib.data.orm.desc.FieldDescription;
import mouse.project.lib.data.orm.desc.FieldDescriptionRecord;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.data.orm.desc.ModelDescriptionImpl;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.TypeValidator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static mouse.project.lib.ioc.injector.Singletons.scanUtils;
@Service
public class ModelDescriptorImpl implements ModelDescriptor {
    @Override
    public <T> ModelDescription<T> describe(Class<T> clazz) {

        TypeValidator typeValidator = new TypeValidator();
        typeValidator.validateCanBeProduced(clazz);

        Optional<Constructor<T>> noArgsOptional = scanUtils().getNoArgsConstructor(clazz);
        noArgsOptional.orElseThrow(() -> new ModelScanException("Model " + clazz + " has no default constructor defined"));

        ModelDescriptionImpl<T> modelDescription = new ModelDescriptionImpl<>(clazz);
        modelDescription.setConstructor(noArgsOptional.get());

        List<Field> annotatedFields = scanUtils().getAnnotatedFields(clazz, NamedColumn.class);
        for (Field field : annotatedFields) {
            if (scanUtils().isCollectionType(field.getType())) {
                throw new ModelScanException("Column cannot be a collection value: " + field);
            }
            FieldDescription fieldDescription = describeField(field);
            modelDescription.addFieldDesc(fieldDescription);
        }

        return modelDescription;
    }

    private FieldDescription describeField(Field field) {
        NamedColumn annotation = field.getAnnotation(NamedColumn.class);
        assert annotation != null;
        String name = annotation.value().toLowerCase();
        if (name.isEmpty()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null || column.name().isEmpty()) {
                name = field.getName();
            } else {
                name = column.name();
            }
        }
        Class<?> type = field.getType();
        return new FieldDescriptionRecord(name, field, type);
    }

}

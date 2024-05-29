package mouse.project.lib.data.orm.scan;

import jakarta.persistence.Column;
import mouse.project.lib.data.exception.ModelScanException;
import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.data.orm.annotation.FromTable;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.lib.data.orm.desc.FieldDescType;
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
import java.util.function.Function;

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

        List<Field> namedFields = scanUtils().getAnnotatedFields(clazz, NamedColumn.class);
        processAnnotatedFields(namedFields, modelDescription, this::describeNamedField);

        List<Field> tableFields = scanUtils().getAnnotatedFields(clazz, FromTable.class);
        processAnnotatedFields(tableFields, modelDescription, this::describeTableField);

        return modelDescription;
    }

    private <T> void processAnnotatedFields(List<Field> annotatedFields,
                                            ModelDescriptionImpl<T> modelDescription,
                                            Function<Field, FieldDescription> mapping) {
        for (Field field : annotatedFields) {
            if (scanUtils().isCollectionType(field.getType())) {
                throw new ModelScanException("Column cannot be a collection value: " + field);
            }
            FieldDescription fieldDescription = mapping.apply(field);
            modelDescription.addFieldDesc(fieldDescription);
        }
    }

    private FieldDescription describeNamedField(Field field) {
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
        return new FieldDescriptionRecord(name, field, type, FieldDescType.COLUMN);
    }

    private FieldDescription describeTableField(Field field) {
        FromTable annotation = field.getAnnotation(FromTable.class);
        assert annotation != null;
        String name = annotation.value().toLowerCase();
        if (name.isEmpty()) {
           throw new ORMException("Cannot find a table with empty name, when processing field: " + field);
        }
        Class<?> type = field.getType();
        if (!isModel(type)) {
            throw new ORMException("Error processing field: " + field + ": " + type + " is not a model");
        }
        return new FieldDescriptionRecord(name, field, type, FieldDescType.MODEL);
    }

    private boolean isModel(Class<?> type) {
        return type.isAnnotationPresent(Model.class);
    }

}

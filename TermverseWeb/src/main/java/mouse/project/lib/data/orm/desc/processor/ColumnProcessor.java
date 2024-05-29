package mouse.project.lib.data.orm.desc.processor;


import mouse.project.lib.data.orm.desc.FieldDescType;
import mouse.project.lib.data.orm.desc.FieldDescription;
import mouse.project.lib.data.orm.desc.FieldDescriptions;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.data.orm.fill.FillUtilService;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Card;
import mouse.project.lib.ioc.annotation.Order;

import java.util.Optional;
import java.util.function.Function;

@Card
@Order(1)
public class ColumnProcessor implements EntityProcessor {
    private final FillUtilService fillUtils;
    @Auto
    public ColumnProcessor(FillUtilService fillUtils) {
        this.fillUtils = fillUtils;
    }
    public void assign(Object entity, Function<Class<?>, Object> eval, ModelDescription<?> modelDescription, String table, String column) {
        FieldDescriptions fields = modelDescription.getFields();
        Optional<FieldDescription> justColumn = fields.getFieldDescriptionByNameAndType(column,  FieldDescType.COLUMN);
        justColumn.ifPresent(j -> fillUtils.assign(entity, eval.apply(j.requiredClass()), j.field()));

        String combo = table + "." + column;
        Optional<FieldDescription> withTable = fields.getFieldDescriptionByNameAndType(combo, FieldDescType.COLUMN);
        withTable.ifPresent(j -> fillUtils.assign(entity, eval.apply(j.requiredClass()), j.field()));
    }
}

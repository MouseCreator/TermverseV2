package mouse.project.lib.data.orm.desc.processor;

import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.data.orm.desc.FieldDescType;
import mouse.project.lib.data.orm.desc.FieldDescription;
import mouse.project.lib.data.orm.desc.FieldDescriptions;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.data.orm.fill.FillUtilService;
import mouse.project.lib.data.orm.map.OrmMap;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Card;
import mouse.project.lib.ioc.annotation.Order;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

@Card
@Order(2)
public class TableProcessor implements EntityProcessor {
    private final OrmMap ormMap;
    private final FillUtilService fillUtilService;
    private final ColumnProcessor columnProcessor;
    @Auto
    public TableProcessor(OrmMap ormMap, FillUtilService fillUtilService, ColumnProcessor columnProcessor) {
        this.ormMap = ormMap;
        this.fillUtilService = fillUtilService;
        this.columnProcessor = columnProcessor;
    }

    public void assign(Object entity, Function<Class<?>, Object> eval, ModelDescription<?> superModelDesc, String table, String column) {
        FieldDescriptions fields = superModelDesc.getFields();
        Optional<FieldDescription> tableDescriptionOpt = fields.getFieldDescriptionByNameAndType(table, FieldDescType.MODEL);
        if (tableDescriptionOpt.isEmpty()) {
            return;
        }
        FieldDescription tableDescription = tableDescriptionOpt.get();
        ModelDescription<?> modelDescription = ormMap.get(tableDescription.requiredClass());
        Field field = tableDescription.field();
        field.setAccessible(true);
        try {
            Object inner = field.get(entity);
            if (inner == null) {
                Constructor<?> constructor = modelDescription.getConstructor();
                Object constructed = fillUtilService.construct(constructor);
                fillUtilService.assign(entity, constructed, field);
                field.set(entity, constructed);
                inner = constructed;
            }
            columnProcessor.assign(inner, eval, modelDescription, table, column);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

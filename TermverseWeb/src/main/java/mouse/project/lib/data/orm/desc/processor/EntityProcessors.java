package mouse.project.lib.data.orm.desc.processor;

import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Card;
import mouse.project.lib.ioc.annotation.Collect;

import java.util.List;
import java.util.function.Function;

@Card
public class EntityProcessors {
    private final List<EntityProcessor> entityProcessorList;
    @Auto
    public EntityProcessors(@Collect(EntityProcessor.class) List<EntityProcessor> entityProcessorList) {
        this.entityProcessorList = entityProcessorList;
    }

    public void assign(Object entity, Function<Class<?>, Object> value, ModelDescription<?> modelDescription, String table, String column) {
        for (EntityProcessor entityProcessor : entityProcessorList) {
            entityProcessor.assign(entity, value, modelDescription, table, column);
        }
    }
}

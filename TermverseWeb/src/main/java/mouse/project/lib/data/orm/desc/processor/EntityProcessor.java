package mouse.project.lib.data.orm.desc.processor;

import mouse.project.lib.data.orm.desc.ModelDescription;

import java.util.function.Function;

public interface EntityProcessor {
    void assign(Object entity, Function<Class<?>, Object> eval, ModelDescription<?> modelDescription, String table, String column);
}

package mouse.project.lib.data.orm.map;

import mouse.project.lib.data.orm.desc.ModelDescription;

public interface OrmMap {
    void put(ModelDescription<?> modelDescription);
    ModelDescription<?> get(Class<?> clazz);
}

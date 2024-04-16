package mouse.project.lib.data.orm.map;

import mouse.project.lib.data.exception.ORMException;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.ioc.annotation.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class OrmMapImpl implements OrmMap {

    private final Map<Class<?>, ModelDescription<?>> map;
    public OrmMapImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public void put(ModelDescription<?> modelDescription) {
        map.put(modelDescription.forClass(), modelDescription);
    }

    @Override
    public ModelDescription<?> get(Class<?> clazz) {
        ModelDescription<?> modelDescription = map.get(clazz);
        if (modelDescription == null) {
            throw new ORMException("No description provided for: " + clazz);
        }
        return modelDescription;
    }
}

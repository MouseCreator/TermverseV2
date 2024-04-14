package mouse.project.termverseweb.models;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@mouse.project.lib.ioc.annotation.Service
public class Factories {
    private final Map<Class<?>, Factory> factories;
    @Autowired
    @Auto
    public Factories(@Collect(Factory.class) List<Factory> factories) {
        this.factories = new HashMap<>();
        for (Factory factory : factories) {
            this.factories.put(factory.getClass(), factory);
        }
    }

    public <T extends Factory> T getFactory(Class<T> clazz) {
        Factory factory = factories.get(clazz);
        if (factory == null) {
            throw new IllegalArgumentException(clazz.getName() + " is not a factory");
        }
        return clazz.cast(factory);
    }
}

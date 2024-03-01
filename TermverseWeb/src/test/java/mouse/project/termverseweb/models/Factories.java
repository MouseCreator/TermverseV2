package mouse.project.termverseweb.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Factories {
    private final Map<Class<?>, Factory> factories;
    @Autowired
    public Factories(List<Factory> factories) {
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

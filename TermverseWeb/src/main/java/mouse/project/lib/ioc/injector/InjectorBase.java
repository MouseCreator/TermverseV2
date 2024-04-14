package mouse.project.lib.ioc.injector;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import mouse.project.lib.ioc.injector.builder.InjectorBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
public class InjectorBase {
    private final String name;
    private final Class<?> configurationClass;
    @Setter(AccessLevel.NONE)
    private final Set<Class<?>> includedClasses;

    public InjectorBase(Class<?> configurationClass, String name) {
        this.configurationClass = configurationClass;
        this.name = name;
        includedClasses = new HashSet<>();
    }

    public boolean isNamed() {
        return !name.isEmpty();
    }

    public void include(Class<?> clazz) {
        includedClasses.add(clazz);
    }

    public Injector build() {
        InjectorBuilder builder = new InjectorBuilder();
        return builder.build(this);
    }
}

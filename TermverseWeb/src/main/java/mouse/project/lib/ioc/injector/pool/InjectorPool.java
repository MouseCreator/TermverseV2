package mouse.project.lib.ioc.injector.pool;

import mouse.project.lib.ioc.injector.Injector;

public interface InjectorPool {
    Injector request(Class<?> configuration);
    void free(Class<?> configuration);
}

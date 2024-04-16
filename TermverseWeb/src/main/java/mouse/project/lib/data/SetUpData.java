package mouse.project.lib.data;

import mouse.project.lib.data.orm.init.OrmInitialization;
import mouse.project.lib.exception.ConfigException;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.ioc.annotation.Service;

@Service
public class SetUpData {
    private final OrmInitialization initialization;
    @Auto
    public SetUpData(OrmInitialization initialization) {
        this.initialization = initialization;
    }

    public void scanModels(String modelsPackage) {
        initialization.initialize(modelsPackage);
    }

    public void scanModels(Class<?> config) {
        Configuration annotation = config.getAnnotation(Configuration.class);
        if (annotation == null) {
            throw new ConfigException("Not a config class: " + config);
        }
        scanModels(annotation.basePackage());
    }
}

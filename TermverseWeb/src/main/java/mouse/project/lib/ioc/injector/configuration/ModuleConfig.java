package mouse.project.lib.ioc.injector.configuration;

import mouse.project.lib.data.utils.DataConfig;
import mouse.project.lib.exception.MissingModuleException;
import mouse.project.lib.files.FilesConfig;
import mouse.project.lib.modules.MouseModules;
import mouse.project.lib.tests.TestsConfig;
import mouse.project.lib.web.config.WebConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ModuleConfig {
    private final Function<String, Set<Class<?>>> collector;
    public ModuleConfig(Function<String, Set<Class<?>>> collector) {
        this.collector = collector;
    }

    public Set<Class<?>> getModuleClasses(MouseModules... modules) {
        Set<Class<?>> result = new HashSet<>();
        for (MouseModules module : modules) {
            Set<Class<?>> singleModuleClasses = getSingleModuleClasses(module);
            result.addAll(singleModuleClasses);
        }
        return result;
    }

    private Set<Class<?>> getSingleModuleClasses(MouseModules module) {
        return switch (module) {
            case WEB_MODULE -> collector.apply(WebConfig.basePackage());
            case DATA_MODULE -> collector.apply(DataConfig.basePackage());
            case TEST_MODULE -> collector.apply(TestsConfig.basePackage());
            case FILES_MODULE -> collector.apply(FilesConfig.basePackage());
            default -> throw new MissingModuleException("No module: " + module );
        };
    }
}

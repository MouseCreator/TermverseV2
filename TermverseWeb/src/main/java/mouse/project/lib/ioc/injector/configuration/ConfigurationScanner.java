package mouse.project.lib.ioc.injector.configuration;

import mouse.project.lib.exception.ConfigException;
import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.ioc.annotation.UseRestriction;
import mouse.project.lib.ioc.injector.AnnotationManager;
import mouse.project.lib.ioc.injector.InjectorBase;
import mouse.project.lib.ioc.injector.card.scan.PackageLoader;
import mouse.project.lib.modules.MouseModules;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigurationScanner {
    private final AnnotationManager annotationManager;
    public ConfigurationScanner(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    public InjectorBase scan(Class<?> configClass) {
        Configuration config = configClass.getAnnotation(Configuration.class);
        if (config==null) {
            throw new ConfigException("No @Configuration annotation for " + configClass);
        }
        String name = config.name();
        InjectorBase injectorBase = new InjectorBase(configClass, name);
        scanAndAddAll(injectorBase, config);
        addSpecialClasses(config, injectorBase);
        addSpecialPackages(config, injectorBase);
        processModules(injectorBase, config);
        return injectorBase;
    }

    private void addSpecialPackages(Configuration config, InjectorBase injectorBase) {
        String[] strings = config.includePackages();
        for (String str : strings) {
            includePackage(str, injectorBase);
        }
    }

    private void includePackage(String str, InjectorBase injectorBase) {
        Set<Class<?>> classes = addAllOf(str);
        addClassesExternal(injectorBase, classes);
    }

    private Set<Class<?>> addAllOf(String packageName) {
        PackageLoader loader = new PackageLoader();
        Set<Class<?>> classes = loader.getAllClasses(packageName);
        return new HashSet<>(classes);
    }

    private void processModules(InjectorBase base, Configuration config) {
        MouseModules[] mouseModules = config.includeModules();
        ModuleConfig moduleConfig = new ModuleConfig(this::scanPackage);
        Set<Class<?>> moduleClasses = moduleConfig.getModuleClasses(mouseModules);
        addClassesToIoc(base, moduleClasses);
    }

    private void addSpecialClasses(Configuration config, InjectorBase injectorBase) {
        Class<?>[] classes = config.includeClasses();
        includeAll(injectorBase, Set.of(classes));
    }

    private void includeAll(InjectorBase injectorBase, Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            injectorBase.include(clazz);
        }
    }

    private Set<Class<?>> scanPackage(String basePackage) {
        PackageLoader loader = new PackageLoader();
        List<Class<? extends Annotation>> targetAnnotations = annotationManager.getTargetAnnotations();
        Set<Class<?>> result = new HashSet<>();
        for (Class<? extends Annotation> target : targetAnnotations) {
            Set<Class<?>> annotatedClasses = loader.getAnnotatedClasses(basePackage, target);
            result.addAll(annotatedClasses);
        }
        return result;
    }

    public void scanAndAddAll(InjectorBase injectorBase, Configuration config) {
        String basePackage = config.basePackage();
        scanAndAddAll(injectorBase, basePackage);
    }

    private void scanAndAddAll(InjectorBase injectorBase, String basePackage) {
        Set<Class<?>> classes = scanPackage(basePackage);
        addClassesToIoc(injectorBase, classes);
    }
    private void addClassesExternal(InjectorBase injectorBase, Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (hasUseRestriction(clazz)) {
                addRestricted(clazz, injectorBase);
            } else {
                addToInjectorBase(injectorBase, clazz);
            }
        }
    }
    private void addClassesToIoc(InjectorBase injectorBase, Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            annotationManager.validateService(clazz);
            if (hasUseRestriction(clazz)) {
                addRestricted(clazz, injectorBase);
            } else {
                addToInjectorBase(injectorBase, clazz);
            }
        }
    }

    private void addToInjectorBase(InjectorBase injectorBase, Class<?> clazz) {
        injectorBase.include(clazz);
    }

    private void addRestricted(Class<?> clazz, InjectorBase injectorBase) {
        if (!injectorBase.isNamed()) {
            return;
        }
        UseRestriction restricted = clazz.getAnnotation(UseRestriction.class);
        assert restricted != null;
        String name = injectorBase.getName();
        boolean isInUsedBy = restricted.usedBy().length == 0 || List.of(restricted.usedBy()).contains(name);
        boolean inNotInForbidden = restricted.forbidden().length == 0 || !List.of(restricted.forbidden()).contains(name);
        if (isInUsedBy && inNotInForbidden) {
            addToInjectorBase(injectorBase, clazz);
        }
    }

    private boolean hasUseRestriction(Class<?> clazz) {
        UseRestriction annotation = clazz.getAnnotation(UseRestriction.class);
        if (annotation == null) {
            return false;
        }
        return annotation.usedBy().length > 0 || annotation.forbidden().length > 0;
    }
}

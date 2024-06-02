package mouse.project.lib.ioc.injector;

import mouse.project.lib.exception.ScanException;
import mouse.project.lib.ioc.annotation.Card;
import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.lib.ioc.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotationManagerImpl implements AnnotationManager {

    private final List<Class<? extends Annotation>> targets =
            List.of(
                Controller.class,
                Dao.class,
                Service.class,
                Card.class
            );
    @Override
    public List<Class<? extends Annotation>> getTargetAnnotations() {
        return targets;
    }

    @Override
    public void validateService(Class<?> clazz) {
        if(isService(clazz))
            return;
        throw new ScanException(clazz + " is not a service.");
    }

    private boolean isService(Class<?> clazz) {
        return targets.stream().anyMatch(clazz::isAnnotationPresent);
    }
}

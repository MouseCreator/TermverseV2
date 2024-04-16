package mouse.project.lib.ioc.injector;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationManager {
    List<Class<? extends Annotation>> getTargetAnnotations();
    void validateService(Class<?> clazz);
}

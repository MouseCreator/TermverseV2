package mouse.project.lib.ioc.injector.filter;

import java.lang.annotation.Annotation;

public class HasAnnotationFilter implements ImplementationFilter {
    private final Class<? extends Annotation> annotation;

    public HasAnnotationFilter(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean test(Class<?> aClass) {
        return aClass.isAnnotationPresent(annotation);
    }
}

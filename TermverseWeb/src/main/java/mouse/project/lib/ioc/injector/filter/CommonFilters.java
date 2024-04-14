package mouse.project.lib.ioc.injector.filter;

import mouse.project.lib.ioc.Inj;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

public class CommonFilters {
    public static Collection<Object> getAllAnnotatedWith(Inj inj, Class<? extends Annotation> annotation) {
        return inj.getFiltered(List.of(new HasAnnotationFilter(annotation)));
    }
}

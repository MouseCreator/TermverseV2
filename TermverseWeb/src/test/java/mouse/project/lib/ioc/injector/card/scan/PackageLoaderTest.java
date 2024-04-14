package mouse.project.lib.ioc.injector.card.scan;

import mouse.project.lib.ioc.annotation.Service;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
@Service
class PackageLoaderTest {
    @Service
    private static class Inner {

    }

    @Test
    void testOnSelf() {
        PackageLoader packageLoader = new PackageLoader();
        Set<Class<?>> classes = packageLoader.getAnnotatedClasses(
                "mouse.project.lib.ioc.injector.card.scan",
                Service.class);
        assertTrue(classes.contains(PackageLoaderTest.class));
        assertTrue(classes.contains(Inner.class));
    }
}
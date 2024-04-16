package mouse.project.lib.web.setup;

import mouse.project.lib.ioc.Ioc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Test that all controllers are prepared without errors;
 * Not using the controllers: cannot launch Tomcat from test scope;
 */
class ControllersPrepareTest {

    @Test
    void scanAndStart() {
        Class<ConfigurationClass> config = ConfigurationClass.class;
        ControllersPrepare cp = Ioc.getConfiguredInjector(config).get(ControllersPrepare.class);
        assertDoesNotThrow(() -> cp.prepareControllers(config));
    }
}
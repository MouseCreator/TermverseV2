package mouse.project.lib.tests;

import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.tests.annotation.InitBeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestsInitImplTest {
    @InitBeforeEach
    private Dependency dependency = null;
    @BeforeEach
    void setUp() {
        TestsInit testsInit = Ioc.getConfiguredInjector(ConfigClass.class).get(TestsInit.class);
        testsInit.setUp(this);
    }
    @Configuration(name = "_tests_module", basePackage = "mouse.project.lib.tests")
    private static class ConfigClass {
    }
    @Service
    private static class Dependency {
        public String getStr() {
            return "Hello";
        }
    }

    @Test
    void setUpTest() {
        assertNotNull(dependency);
        assertEquals("Hello", dependency.getStr());
    }
}
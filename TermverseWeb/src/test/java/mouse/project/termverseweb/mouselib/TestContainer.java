package mouse.project.termverseweb.mouselib;

import mouse.project.lib.data.SetUpData;
import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.tests.TestsInit;

public class TestContainer {
    public static Inj get() {
        return Ioc.getConfiguredInjector(TestConfiguration.class);
    }

    private static TestsInit tests() {
        return get().get(TestsInit.class);
    }

    public static void setUp(Object object) {
        tests().setUp(object);
    }

    public static void initializeData() {
        get().get(SetUpData.class).scanModels("mouse.project.termverseweb.model");
    }
}

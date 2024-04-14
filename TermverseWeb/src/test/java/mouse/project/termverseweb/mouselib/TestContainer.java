package mouse.project.termverseweb.mouselib;

import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.Ioc;

public class TestContainer {
    public static Inj get() {
        return Ioc.getConfiguredInjector(TestConfiguration.class);
    }
}

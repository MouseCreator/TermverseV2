package mouse.project.termverseweb.mouse;

import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.Ioc;
import mouse.project.termverseweb.config.MouseProjConfig;

public class Container {
    public static Inj get() {
        return Ioc.getConfiguredInjector(MouseProjConfig.class);
    }
}

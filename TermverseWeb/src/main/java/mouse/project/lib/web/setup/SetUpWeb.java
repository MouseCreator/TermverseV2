package mouse.project.lib.web.setup;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.tomcat.TomcatLauncher;


@Service
public class SetUpWeb {
    private final ControllersPrepare controllersPrepare;
    @Auto
    public SetUpWeb(ControllersPrepare cs) {
        this.controllersPrepare = cs;
    }

    public void scanAndStart(Class<?> config) {
        controllersPrepare.prepareControllers(config);
        TomcatLauncher tomcatLauncher = new TomcatLauncher();
        try {
            tomcatLauncher.launch(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch Tomcat", e);
        }
    }


}

package mouse.project.termverseweb.mouse;

import mouse.project.lib.data.SetUpData;
import mouse.project.lib.web.setup.SetUpWeb;

public class MouseAppSetup {
    public void start() {
        Container.get().get(SetUpData.class).scanModels("mouse.project.termverseweb.model");
        Container.get().get(SetUpWeb.class).scanAndStart(Container.base());
    }
}

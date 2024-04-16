package mouse.project.termverseweb.config;

import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.modules.MouseModules;

@Configuration(
        basePackage = "mouse.project.termverseweb",
        includeModules = {MouseModules.DATA_MODULE, MouseModules.WEB_MODULE},
        name = "main-app"
)
public class MouseProjConfig {
}

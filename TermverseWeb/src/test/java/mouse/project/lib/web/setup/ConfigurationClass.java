package mouse.project.lib.web.setup;

import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.modules.MouseModules;

@Configuration(basePackage = "mouse.project.lib.web.setup", includeModules = MouseModules.WEB_MODULE)
public class ConfigurationClass {
}

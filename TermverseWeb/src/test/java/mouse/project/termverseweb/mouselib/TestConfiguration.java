package mouse.project.termverseweb.mouselib;

import mouse.project.lib.ioc.annotation.Configuration;
import mouse.project.lib.modules.MouseModules;


@Configuration(
        basePackage = "mouse.project.termverseweb",
        name = "test_data",
        includeModules = {MouseModules.DATA_MODULE, MouseModules.TEST_MODULE},
        includePackages = "mouse.project.termverseweb.mapper.impl"
)
public class TestConfiguration {
}

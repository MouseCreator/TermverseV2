package mouse.project.lib.ioc.base;

import mouse.project.lib.ioc.annotation.Factory;
import mouse.project.lib.ioc.annotation.Order;
import mouse.project.lib.ioc.annotation.Primary;
import mouse.project.lib.ioc.annotation.Service;

@Service
public class ServiceFactory {
    @Primary
    @Factory
    @Order(1)
    public ServiceInterface getPrimaryService() {
        return () -> "First";
    }
}

package mouse.project.lib.ioc.base;

import mouse.project.lib.ioc.annotation.Order;
import mouse.project.lib.ioc.annotation.Service;

@Service
@Order(2)
public class SecondImplementation implements ServiceInterface {
    @Override
    public String getString() {
        return "Second";
    }
}

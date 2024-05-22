package mouse.project.termverseweb.mouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import mouse.project.lib.ioc.annotation.Factory;
import mouse.project.lib.ioc.annotation.Service;

@Service
public class MouseObjects {
    @Factory
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}

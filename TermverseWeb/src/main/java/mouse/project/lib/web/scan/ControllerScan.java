package mouse.project.lib.web.scan;

import java.util.Collection;

public interface ControllerScan {
    Collection<Registration> scanControllers(Collection<Object> allControllers);
    Collection<Registration> scanController(Object controller);
}

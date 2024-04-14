package mouse.project.lib.ioc.base;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.annotation.UseRestriction;

@UseRestriction(usedBy = "Release")
@Service
public class Restricted {
    public int getInteger() {
        return 1;
    }
}

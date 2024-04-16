package mouse.project.lib.ioc.base;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.annotation.UseRestriction;

@UseRestriction(forbidden = "Release")
@Service
public class ForbiddenToRelease {
}

package mouse.project.lib.web.invoker.desc;

import mouse.project.lib.web.tool.FullURL;

public interface URLArgumentDesc extends ArgumentDesc {
    FullURL getPattern();
    Class<?> expectedType();
    String getTarget();
}

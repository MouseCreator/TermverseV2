package mouse.project.lib.web.request;


import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

import java.util.Map;

public interface RequestURL {
    FullURL getURL();
    RequestMethod method();
    RequestBody getBody();
    Map<String, Object> attributes();
}

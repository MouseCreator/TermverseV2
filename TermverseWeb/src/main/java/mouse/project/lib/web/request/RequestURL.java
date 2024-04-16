package mouse.project.lib.web.request;


import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

public interface RequestURL {
    FullURL getURL();
    RequestMethod method();
    RequestBody getBody();
}

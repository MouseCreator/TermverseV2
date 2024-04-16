package mouse.project.lib.web.invoker.processor;

import mouse.project.lib.web.invoker.desc.URLArgumentDesc;
import mouse.project.lib.web.request.RequestURL;

public interface URLArgumentProcessor extends ArgumentProcessor{
    Object process(URLArgumentDesc argumentDesc, RequestURL requestURL);
}

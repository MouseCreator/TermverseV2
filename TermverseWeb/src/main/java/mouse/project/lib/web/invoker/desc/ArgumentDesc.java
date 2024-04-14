package mouse.project.lib.web.invoker.desc;

import mouse.project.lib.web.request.RequestURL;

public interface ArgumentDesc {
    Object apply(RequestURL requestURL);
}

package mouse.project.lib.web.invoker.processor;

import mouse.project.lib.web.invoker.desc.BodyDesc;
import mouse.project.lib.web.request.RequestURL;

public interface BodyArgumentProcessor extends ArgumentProcessor {
    Object process(BodyDesc argumentDesc, RequestURL requestURL);
}

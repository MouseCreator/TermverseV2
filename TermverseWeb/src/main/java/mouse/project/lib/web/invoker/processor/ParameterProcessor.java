package mouse.project.lib.web.invoker.processor;

import mouse.project.lib.web.invoker.desc.ParameterDesc;
import mouse.project.lib.web.request.RequestURL;

public interface ParameterProcessor extends ArgumentProcessor {
    Object process(ParameterDesc parameterDesc, RequestURL requestURL);
}

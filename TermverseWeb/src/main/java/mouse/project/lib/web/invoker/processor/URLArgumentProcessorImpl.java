package mouse.project.lib.web.invoker.processor;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.invoker.ParamTranslation;
import mouse.project.lib.web.invoker.desc.URLArgumentDesc;
import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLService;

@Service
public class URLArgumentProcessorImpl implements URLArgumentProcessor {

    private final ParamTranslation translation;
    private final URLService urlService;
    @Auto
    public URLArgumentProcessorImpl(ParamTranslation translation, URLService urlService) {
        this.translation = translation;
        this.urlService = urlService;
    }
    @Override
    public Object process(URLArgumentDesc argumentDesc, RequestURL requestURL) {
        FullURL fullUrl = requestURL.getURL();
        String target = argumentDesc.getTarget();
        FullURL pattern = argumentDesc.getPattern();
        String pathArgument = urlService.getPathArgument(pattern.path(), fullUrl.path(), target);
        return translation.translate(pathArgument, argumentDesc.expectedType());
    }
}

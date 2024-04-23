package mouse.project.lib.web.dispatcher;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.RequestProcessException;
import mouse.project.lib.web.mapper.URLTransform;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.request.RequestBody;
import mouse.project.lib.web.request.RequestBodyImpl;
import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.request.RequestURLImpl;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.stream.Collectors;
@Service
public class RequestPreinitializer {
    private final URLService urlService;
    @Auto
    public RequestPreinitializer(URLService urlService) {
        this.urlService = urlService;
    }

    public RequestURL createRequest(HttpServletRequest req) {
        FullURL fullURL = getRequestUrl(req);
        String methodString = req.getMethod();
        RequestMethod method = RequestMethod.fromString(methodString);
        RequestBody requestBody;
        try {
            String bodyString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            requestBody = new RequestBodyImpl(bodyString);
        } catch (IOException e) {
            throw new RequestProcessException(e);
        }

        HashMap<String, Object> map = createMap(req);
        return new RequestURLImpl(fullURL, method, requestBody, map);
    }

    public FullURL getRequestUrl(HttpServletRequest req) {
        String strUrl = URLTransform.getFullURL(req);
        return urlService.create(strUrl);
    }

    private HashMap<String, Object> createMap(HttpServletRequest req) {
        HashMap<String, Object> attributeMap = new HashMap<>();

        Enumeration<String> attributeNames = req.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = req.getAttribute(attributeName);
            attributeMap.put(attributeName, attributeValue);
        }

        return attributeMap;
    }
}

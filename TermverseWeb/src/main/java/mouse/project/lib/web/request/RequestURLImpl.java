package mouse.project.lib.web.request;

import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

import java.util.HashMap;
import java.util.Map;

public class RequestURLImpl implements RequestURL {
    private final FullURL fullURL;
    private final RequestMethod method;
    private final RequestBody body;
    private final HashMap<String, Object> attributes;

    public RequestURLImpl(FullURL fullURL, RequestMethod method, RequestBody body, HashMap<String, Object> map) {
        this.fullURL = fullURL;
        this.method = method;
        this.body = body;
        this.attributes = map;
    }

    @Override
    public FullURL getURL() {
        return fullURL;
    }

    @Override
    public RequestMethod method() {
        return method;
    }

    @Override
    public RequestBody getBody() {
        return body;
    }

    @Override
    public Map<String, Object> attributes() {
        return new HashMap<>(attributes);
    }
}

package mouse.project.lib.web.request;

import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

public class RequestURLImpl implements RequestURL {
    private final FullURL fullURL;
    private final RequestMethod method;
    private final RequestBody body;

    public RequestURLImpl(FullURL fullURL, RequestMethod method, RequestBody body) {
        this.fullURL = fullURL;
        this.method = method;
        this.body = body;
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
}

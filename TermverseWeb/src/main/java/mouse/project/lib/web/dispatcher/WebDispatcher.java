package mouse.project.lib.web.dispatcher;

import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.response.WebResponse;

public interface WebDispatcher {
    WebResponse onRequest(RequestURL requestURL);

    void useMap(DispatcherMap dispatcherMap);
}

package mouse.project.lib.web.context;

import mouse.project.lib.web.dispatcher.WebDispatcher;

public interface WebContext {
    WebDispatcher getDispatcher(Class<?> configuration);
    void setDispatcher(Class<?> configuration, WebDispatcher dispatcher);
}

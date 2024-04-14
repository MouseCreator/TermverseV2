package mouse.project.lib.web.context;

import mouse.project.lib.web.tool.FullURL;

public class ControllerContextImpl implements ControllerContext {

    private final Class<?> root;
    private final FullURL urlTool;
    private final Object instance;
    public ControllerContextImpl(Object instance, FullURL urlTool) {
        this.root = instance.getClass();
        this.instance = instance;
        this.urlTool = urlTool;
    }
    @Override
    public FullURL getUrl() {
        return urlTool;
    }

    @Override
    public Class<?> getRoot() {
        return root;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
}

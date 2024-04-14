package mouse.project.lib.web.context;

public interface ControllerContext extends URLHolder {
    Class<?> getRoot();
    Object getInstance();
}

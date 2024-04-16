package mouse.project.lib.ioc.injector;

public class Singletons {
    public static ScanUtils scanUtils() {
        return ScanUtils.getInstance();
    }
}

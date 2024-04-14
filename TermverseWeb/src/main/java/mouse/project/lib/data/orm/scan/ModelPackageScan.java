package mouse.project.lib.data.orm.scan;

import java.util.Collection;

public interface ModelPackageScan {
    Collection<Class<?>> getAllModels(String packageName);
}

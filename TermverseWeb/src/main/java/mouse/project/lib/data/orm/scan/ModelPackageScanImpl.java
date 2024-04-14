package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.card.scan.PackageLoader;

import java.util.Collection;
@Service
public class ModelPackageScanImpl implements ModelPackageScan {
    @Override
    public Collection<Class<?>> getAllModels(String packageName) {
        PackageLoader packageLoader = new PackageLoader();
        return packageLoader.getAnnotatedClasses(packageName, Model.class);
    }
}

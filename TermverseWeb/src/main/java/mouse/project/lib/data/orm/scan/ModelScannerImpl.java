package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ModelScannerImpl implements ModelScanner {
    private final ModelPackageScan packageScan;
    private final ModelDescriptor descriptor;
    @Auto
    public ModelScannerImpl(ModelPackageScan packageScan, ModelDescriptor descriptor) {
        this.packageScan = packageScan;
        this.descriptor = descriptor;
    }

    @Override
    public List<ModelDescription<?>> scanAndDescribe(String packageName) {
        Collection<Class<?>> allModels = packageScan.getAllModels(packageName);
        List<ModelDescription<?>> result = new ArrayList<>();
        for (Class<?> mClass : allModels) {
            ModelDescription<?> described = descriptor.describe(mClass);
            result.add(described);
        }
        return result;
    }
}

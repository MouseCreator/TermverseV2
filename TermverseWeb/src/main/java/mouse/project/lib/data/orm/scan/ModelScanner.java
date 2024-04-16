package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.orm.desc.ModelDescription;

import java.util.List;

public interface ModelScanner {
    List<ModelDescription<?>> scanAndDescribe(String packageName);
}

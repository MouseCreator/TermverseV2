package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.orm.desc.ModelDescription;

public interface ModelDescriptor {
    <T> ModelDescription<T> describe(Class<T> clazz);
}

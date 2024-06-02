package mouse.project.lib.data.orm.desc;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModelDescriptionImpl<T> implements ModelDescription<T> {

    private final List<FieldDescription> fieldDescriptions;
    @Setter
    @Getter
    private Constructor<T> constructor = null;
    private final Class<T> instanceClass;

    public ModelDescriptionImpl(Class<T> instanceClass) {
        this.instanceClass = instanceClass;
        this.fieldDescriptions = new ArrayList<>();
    }

    @Override
    public FieldDescriptionsMap getFields() {
        return FieldDescriptionsMap.of(fieldDescriptions);
    }

    @Override
    public Class<T> forClass() {
        return instanceClass;
    }

    public void addFieldDesc(FieldDescription fieldDescription) {
        fieldDescriptions.add(fieldDescription);
    }
}

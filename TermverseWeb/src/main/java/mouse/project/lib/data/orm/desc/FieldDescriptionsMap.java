package mouse.project.lib.data.orm.desc;

import java.util.*;

public class FieldDescriptionsMap implements FieldDescriptions {
    private FieldDescriptionsMap() {
        map = new HashMap<>();
    }
    private final Map<String, FieldDescription> map;
    public static FieldDescriptionsMap of(List<FieldDescription> fieldDescriptions) {
        FieldDescriptionsMap fieldDescriptionsMap = new FieldDescriptionsMap();
        for (FieldDescription fieldDescription : fieldDescriptions) {
            String name = fieldDescription.columnName();
            fieldDescriptionsMap.map.put(name, fieldDescription);
        }
        return fieldDescriptionsMap;
    }

    @Override
    public List<FieldDescription> getDescriptions() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<FieldDescription> getFieldDescriptionByName(String name) {
        FieldDescription fieldDescription = map.get(name);
        return Optional.ofNullable(fieldDescription);
    }
}

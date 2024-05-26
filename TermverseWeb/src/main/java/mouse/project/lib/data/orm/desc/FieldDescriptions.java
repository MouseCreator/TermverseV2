package mouse.project.lib.data.orm.desc;

import java.util.List;
import java.util.Optional;

public interface FieldDescriptions {
    List<FieldDescription> getDescriptions();
    Optional<FieldDescription> getFieldDescriptionByName(String name);
}

package mouse.project.lib.files;

import java.util.Optional;

public interface PropertyMap {
    String getPropertyValue(String key);
    Optional<String> findPropertyValue(String key);
}

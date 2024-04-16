package mouse.project.lib.files;

import mouse.project.lib.files.exception.NoSuchPropertyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyMapImpl implements PropertyMap, MapFiller {
    private final Map<String, String> map;

    public PropertyMapImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public String getPropertyValue(String key) {
        String value = map.get(key);
        if (value == null) {
            throw new NoSuchPropertyException("No property by key: " + key);
        }
        return value;
    }

    @Override
    public Optional<String> findPropertyValue(String key) {
        String s = map.get(key);
        return Optional.ofNullable(s);
    }
    @Override
    public void put(String key, String value) {
        map.put(key, value);
    }
}

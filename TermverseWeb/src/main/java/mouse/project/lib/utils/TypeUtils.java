package mouse.project.lib.utils;

import java.util.Collection;

public class TypeUtils {
    public static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }
}

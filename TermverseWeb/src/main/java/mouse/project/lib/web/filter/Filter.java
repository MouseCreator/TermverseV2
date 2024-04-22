package mouse.project.lib.web.filter;

import java.util.List;

public interface Filter {
    boolean invoke();
    default List<String> alwaysApplyToUrls() {
        return List.of("/*");
    };
    default List<String> neverApplyToUrls() {
        return List.of();
    }
}

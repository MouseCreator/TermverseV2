package mouse.project.lib.web.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface MFilter {
    boolean invoke(HttpServletRequest request, HttpServletResponse response);
    default List<String> alwaysApplyToUrls() {
        return List.of("/*");
    };
    default List<String> neverApplyToUrls() {
        return List.of();
    }
}

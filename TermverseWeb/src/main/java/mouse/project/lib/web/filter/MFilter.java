package mouse.project.lib.web.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MFilter {
    boolean invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException;
    default List<String> alwaysApplyToUrls() {
        return List.of("/*");
    };
    default List<String> neverApplyToUrls() {
        return List.of();
    }
}

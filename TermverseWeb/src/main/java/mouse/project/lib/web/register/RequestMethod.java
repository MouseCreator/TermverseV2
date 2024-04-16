package mouse.project.lib.web.register;


import java.util.Collection;
import java.util.List;

public enum RequestMethod {
    POST, GET, UPDATE, DELETE;

    public static Collection<RequestMethod> all() {
        return List.of(POST, GET, UPDATE, DELETE);
    }
}

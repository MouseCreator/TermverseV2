package mouse.project.lib.web.register;


import java.util.Collection;
import java.util.List;

public enum RequestMethod {
    POST, GET, UPDATE, DELETE;

    public static Collection<RequestMethod> all() {
        return List.of(POST, GET, UPDATE, DELETE);
    }

    public static RequestMethod fromString(String methodString) {
        return switch (methodString) {
            case "POST" -> POST;
            case "GET" -> GET;
            case "UPDATE" -> UPDATE;
            case "DELETE" -> DELETE;
            default -> throw new IllegalArgumentException("Unexpected request method: " + methodString);
        };
    }
}

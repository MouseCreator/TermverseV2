package mouse.project.lib.web.response;

import lombok.Data;

@Data
public class MouseResponse<T> {

    private T body;
    int status;

    private MouseResponse(int status, T body) {
        this.body = body;
        this.status = status;
    }

    public static MouseResponse<Void> of(int status) {
        return new MouseResponse<>(status, null);
    }
    public static <T> MouseResponse<T> of(int status, T body) {
        return new MouseResponse<>(status, body);
    }
}

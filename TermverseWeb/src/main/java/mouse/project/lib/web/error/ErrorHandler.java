package mouse.project.lib.web.error;

import mouse.project.lib.web.response.ErrorResponse;

public interface ErrorHandler<T extends RuntimeException> {
    void handle(T exception, ErrorResponse response);
    Class<T> forType();
    default void handleDefault(RuntimeException e, ErrorResponse response) {
        T castedException = forType().cast(e);
        handle(castedException, response);
    }
}

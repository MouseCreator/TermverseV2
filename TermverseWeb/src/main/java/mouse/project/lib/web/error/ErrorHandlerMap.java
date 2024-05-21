package mouse.project.lib.web.error;

import mouse.project.lib.ioc.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class ErrorHandlerMap {
    private final Map<Class<?>, ErrorHandler<?>> typeToHandlerMap;
    @Auto
    public ErrorHandlerMap() {
        typeToHandlerMap = new HashMap<>();
    }
    @After
    public void assignAll(@Collect(ErrorHandler.class) List<ErrorHandler<?>> errorHandlerList) {
        errorHandlerList.forEach(
                e -> typeToHandlerMap.put(e.forType(), e)
        );
    }
    public <T extends RuntimeException> Optional<ErrorHandler<?>> onException(T exception) {
        Class<?> clazz = exception.getClass();
        while (clazz != Exception.class) {
            ErrorHandler<?> errorHandler = typeToHandlerMap.get(clazz);
            if (errorHandler != null) {
                return Optional.of(errorHandler);
            }
            clazz = clazz.getSuperclass();
        }
        return Optional.empty();
    }
}

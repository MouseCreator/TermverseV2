package mouse.project.lib.web.invoker.desc;

import java.util.Optional;

public interface ParameterDesc extends ArgumentDesc {
    Class<?> expectedType();
    String name();
    Optional<String> defaultValue();
}

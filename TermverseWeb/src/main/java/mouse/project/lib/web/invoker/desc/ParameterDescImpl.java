package mouse.project.lib.web.invoker.desc;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import mouse.project.lib.web.invoker.processor.ParameterProcessor;
import mouse.project.lib.web.request.RequestURL;

import java.util.Optional;
@EqualsAndHashCode
@ToString
public final class ParameterDescImpl implements ParameterDesc {
    private final String name;
    private final Class<?> expectedType;
    private final String defaultValue;
    private final ParameterProcessor parameterProcessor;

    public ParameterDescImpl(Class<?> type, String name, String defaultValue, ParameterProcessor parameterProcessor) {
        this.expectedType = type;
        this.name = name;
        this.defaultValue = defaultValue;
        this.parameterProcessor = parameterProcessor;
    }

    @Override
    public Optional<String> defaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<?> expectedType() {
        return expectedType;
    }


    @Override
    public Object apply(RequestURL requestURL) {
        return parameterProcessor.process(this, requestURL);
    }
}

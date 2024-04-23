package mouse.project.lib.web.invoker.desc;

import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.request.RequestURL;

public class AttributeDescImpl implements AttributeDesc {
    private final String attributeName;
    private final Class<?> expectedType;

    public AttributeDescImpl(String attributeName, Class<?> expectedType) {
        this.attributeName = attributeName;
        this.expectedType = expectedType;
    }

    @Override
    public Object apply(RequestURL requestURL) {
        Object obj = requestURL.attributes().get(attributeName);
        if (obj == null) {
            throw new ControllerException("No request attribute: " + attributeName);
        }
        return obj;
    }

    @Override
    public Class<?> expectedType() {
        return expectedType;
    }

    @Override
    public String getAttribute() {
        return attributeName;
    }
}

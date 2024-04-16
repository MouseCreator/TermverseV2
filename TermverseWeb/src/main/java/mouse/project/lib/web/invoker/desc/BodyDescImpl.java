package mouse.project.lib.web.invoker.desc;

import mouse.project.lib.web.invoker.processor.BodyArgumentProcessor;
import mouse.project.lib.web.request.RequestURL;

public class BodyDescImpl implements BodyDesc {

    private final Class<?> collectionType;
    private final Class<?> expectedType;
    private final String name;
    private final BodyArgumentProcessor processor;

    public BodyDescImpl(Class<?> collectionType,
                        Class<?> expectedType,
                        String name,
                        BodyArgumentProcessor processor) {
        this.collectionType = collectionType;
        this.expectedType = expectedType;
        this.name = name;
        this.processor = processor;
    }

    public BodyDescImpl(Class<?> expectedType,
                        String name, BodyArgumentProcessor processor) {
        this.expectedType = expectedType;
        this.name = name;
        this.processor = processor;
        collectionType = null;
    }

    @Override
    public Class<?> expectedClass() {
        return expectedType;
    }

    @Override
    public boolean isCollection() {
        return collectionType != null;
    }

    @Override
    public boolean isFullBody() {
        return name == null || name.isEmpty();
    }

    @Override
    public Class<?> collectionType() {
        return collectionType;
    }

    @Override
    public String attributeName() {
        return name;
    }

    @Override
    public Object apply(RequestURL requestURL) {
        return processor.process(this, requestURL);
    }
}

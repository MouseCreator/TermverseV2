package mouse.project.lib.web.invoker.processor;

import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.ioc.injector.card.helper.CollectionProducer;
import mouse.project.lib.web.invoker.desc.BodyDesc;
import mouse.project.lib.web.parse.BodyParser;
import mouse.project.lib.web.request.RequestBody;
import mouse.project.lib.web.request.RequestURL;

import java.util.Collection;
@Service
public class BodyArgumentProcessorImpl implements BodyArgumentProcessor {
    private final BodyParser parser;
    @Override
    public Object process(BodyDesc argumentDesc, RequestURL requestURL) {
        return getBodyInfo(requestURL.getBody(), argumentDesc);
    }
    @Auto
    public BodyArgumentProcessorImpl(BodyParser parser) {
        this.parser = parser;
    }

    public Object getBodyInfo(RequestBody body, BodyDesc desc) {
        CollectionProducer collectionProducer = new CollectionProducer();
        String str = body.get();
        String attr = desc.attributeName();
        Object param;
        if (attr == null || attr.isEmpty()) {
            if (desc.isCollection()) {
                Collection<?> parsedValues = parser.parseAll(str, desc.expectedClass());
                param = collectionProducer.create(desc.collectionType(), parsedValues);
            } else {
                param = parser.parse(str, desc.expectedClass());
            }
        } else {
            if (desc.isCollection()) {
                Collection<?> parsedValues = parser.parseAllByAttribute(str, attr, desc.expectedClass());
                param = collectionProducer.create(desc.collectionType(), parsedValues);
            } else {
                param = parser.parseAttribute(str, attr, desc.expectedClass());
            }
        }
        return param;
    }
}

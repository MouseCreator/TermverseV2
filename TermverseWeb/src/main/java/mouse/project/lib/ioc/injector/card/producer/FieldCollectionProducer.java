package mouse.project.lib.ioc.injector.card.producer;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.helper.CollectionProducer;
import mouse.project.lib.ioc.injector.card.invoke.FieldInjection;

import java.util.Collection;

public class FieldCollectionProducer implements FieldProducer{
    private final FieldInjection fieldInjection;

    private final Class<?> collectionClass;

    public FieldCollectionProducer(FieldInjection fieldInjection, Class<?> collectionClass) {
        this.fieldInjection = fieldInjection;
        this.collectionClass = collectionClass;
    }

    @Override
    public void setField(Object obj, CardAccess container) {
        Implementation<?> requiredType = fieldInjection.getRequiredType();
        Collection<?> implementations = container.getAllImplementations(requiredType);
        Collection<?> typeCollection = createCollection(implementations);
        fieldInjection.setField(obj, typeCollection);
    }

    private Collection<?> createCollection(Collection<?> implementations) {
        CollectionProducer producer = new CollectionProducer();
        return producer.create(collectionClass, implementations);
    }
}

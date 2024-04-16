package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.access.CardAccess;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.producer.CardProducer;

import java.util.List;
import java.util.function.Supplier;

public class SupplierDefinitionImpl<T> implements SupplierDefinition<T> {

    private final Supplier<T> supplier;
    private final Implementation<T> tImplementation;

    public SupplierDefinitionImpl(Supplier<T> supplier, Implementation<T> type) {
        this.supplier = supplier;
        this.tImplementation = type;
    }

    @Override
    public CardProducer<T> getProducer() {
        return new CardProducer<>() {
            @Override
            public T produce(CardAccess container) {
                return supplier.get();
            }
            @Override
            public void afterConstruction(Object constructed, CardAccess container) {
            }
        };
    }

    @Override
    public List<Implementation<?>> requiredImplementations() {
        return List.of();
    }

    @Override
    public Implementation<T> getType() {
        return tImplementation;
    }
}

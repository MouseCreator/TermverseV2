package mouse.project.lib.ioc.injector.card.definition;

import mouse.project.lib.ioc.injector.card.producer.CardProducer;
import mouse.project.lib.ioc.injector.map.TypeHolder;

public interface CardDefinition<T> extends Definition, TypeHolder<T> {
    CardProducer<T> getProducer();
}

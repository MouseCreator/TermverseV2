package mouse.project.lib.ioc.injector.card.scan;

import mouse.project.lib.ioc.injector.card.definition.DefinedCard;

public interface CardScanner {
    <T> DefinedCard<T> scan(Class<T> tClass);
}

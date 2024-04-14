package mouse.project.lib.ioc.injector;

import mouse.project.lib.ioc.injector.card.Cards;

public interface Injector {
    String getName();
    Class<?> getConfigurationClass();
    Cards getCards();
}

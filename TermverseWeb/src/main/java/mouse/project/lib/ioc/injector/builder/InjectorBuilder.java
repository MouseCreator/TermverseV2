package mouse.project.lib.ioc.injector.builder;

import mouse.project.lib.ioc.injector.Injector;
import mouse.project.lib.ioc.injector.InjectorBase;
import mouse.project.lib.ioc.injector.InjectorImpl;
import mouse.project.lib.ioc.injector.card.Cards;

import java.util.Set;

public class InjectorBuilder {
    public Injector build(InjectorBase injectorBase) {
        InjectorImpl result = new InjectorImpl();

        result.setConfigurationClass(injectorBase.getConfigurationClass());
        result.setName(injectorBase.getName());
        Set<Class<?>> includedClasses = injectorBase.getIncludedClasses();
        Cards cards = Cards.create();

        cards.init(includedClasses);
        result.setCards(cards);
        return result;

    }
}

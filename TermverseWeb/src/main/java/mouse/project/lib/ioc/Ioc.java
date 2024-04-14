package mouse.project.lib.ioc;

import mouse.project.lib.ioc.injector.card.Cards;
import mouse.project.lib.ioc.injector.pool.InjectorPool;
import mouse.project.lib.ioc.injector.pool.InjectorPoolImpl;

public class Ioc {
    private final InjectorPool injectorPool;

    private Ioc() {
        this.injectorPool = new InjectorPoolImpl();
    }

    private static Ioc ioc = null;

    public static Inj getConfiguredInjector(Class<?> config) {
        if (ioc == null) {
            ioc = new Ioc();
        }
        Cards cards = ioc.injectorPool.request(config).getCards();
        return new InjImpl(cards);
    }
}

package mouse.project.lib.ioc.injector.card.container;

import mouse.project.lib.ioc.injector.map.BuiltHolder;
import mouse.project.lib.ioc.injector.map.BuiltHolderImpl;
import mouse.project.lib.ioc.injector.map.DefinedMap;

public class CardContainerImpl implements CardContainer {

    public CardContainerImpl(DefinedMap<BuiltHolder<?>> definedMap) {
        this.definedMap = definedMap;
    }

    private final DefinedMap<BuiltHolder<?>> definedMap;
    @Override
    public <T> boolean containsImplementation(Implementation<T> implementation) {
        return definedMap.contains(implementation);
    }

    @Override
    public <T> T findImplementation(Implementation<T> implementation) {
        Class<T> clazz = implementation.getClazz();
        return clazz.cast(definedMap.lookup(implementation).getBuilt());
    }
    @Override
    public <T> void put( Object obj) {
        Implementation<?> implementation = Implementations.create(obj.getClass());
        BuiltHolder<?> builtHolder = new BuiltHolderImpl<>(implementation, obj);
        definedMap.add(builtHolder);
    }
}

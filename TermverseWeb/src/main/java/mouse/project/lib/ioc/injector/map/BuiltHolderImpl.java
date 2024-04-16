package mouse.project.lib.ioc.injector.map;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public class BuiltHolderImpl<T> implements BuiltHolder<T> {
    private final Implementation<T> implementation;
    private final Object object;
    public BuiltHolderImpl(Implementation<T> implementation, Object object) {
        this.implementation = implementation;
        this.object = object;
    }

    @Override
    public Object getBuilt() {
        return object;
    }

    @Override
    public Implementation<T> getType() {
        return implementation;
    }
}

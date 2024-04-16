package mouse.project.lib.ioc.injector.map;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public interface TypeHolder<T> {
    Implementation<T> getType();
}

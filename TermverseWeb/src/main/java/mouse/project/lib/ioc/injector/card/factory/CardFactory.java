package mouse.project.lib.ioc.injector.card.factory;

import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.filter.ImplementationFilter;

import java.util.Collection;

public interface CardFactory {
    <T> T buildCard(Implementation<T> implementation);
    <T> Collection<T> buildAllCards(Implementation<T> implementation);
    <T> T buildCard(Implementation<T> implementation, BuildStack buildStack);
    <T> Collection<T> buildAllCards(Implementation<T> implementation, BuildStack buildStack);
    Collection<Object> getFiltered(Collection<ImplementationFilter> filters);
}

package mouse.project.lib.ioc.injector.card.container;

public interface CardContainer {
    <T> boolean containsImplementation(Implementation<T> implementation);
    <T> T findImplementation(Implementation<T> implementation);
    <T> void put(Object obj);
}

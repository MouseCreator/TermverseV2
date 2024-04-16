package mouse.project.lib.ioc.injector.card.definition;



public interface FactoryCard<T> extends CardDefinition<T> {
    MethodDefinition getFactoryMethod();
}

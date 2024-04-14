package mouse.project.lib.ioc.injector.card.producer;

public interface FactoryMethodProducer<T> extends CardProducer<T>{
    void setMethod(MethodProducer method);
}

package mouse.project.lib.ioc.injector.card.producer;

public interface DefinedProducer<T> extends CardProducer<T> {
    void setConstructor(ConstructorProducer<T> constructor);
    void addSetter(SetterProducer setterProducer);
    void addFieldInjection(FieldProducer fieldProducer);
    void addAction(ActionProducer actionProducer);
}

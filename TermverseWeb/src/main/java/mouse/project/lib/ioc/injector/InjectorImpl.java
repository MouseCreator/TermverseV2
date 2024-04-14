package mouse.project.lib.ioc.injector;

import lombok.Getter;
import lombok.Setter;
import mouse.project.lib.ioc.injector.card.Cards;

@Getter
@Setter
public class InjectorImpl implements Injector {
    private Class<?> configurationClass;
    private String name;
    private Cards cards;
}

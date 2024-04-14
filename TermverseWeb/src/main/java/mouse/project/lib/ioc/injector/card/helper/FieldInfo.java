package mouse.project.lib.ioc.injector.card.helper;

import mouse.project.lib.ioc.injector.card.container.Implementation;

public record FieldInfo(Implementation<?> implementation, Class<?> collectionType) {
}

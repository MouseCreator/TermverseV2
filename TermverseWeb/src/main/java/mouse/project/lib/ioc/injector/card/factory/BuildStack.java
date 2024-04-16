package mouse.project.lib.ioc.injector.card.factory;

import mouse.project.lib.ioc.injector.card.container.Implementation;

import java.util.ArrayList;
import java.util.List;

public class BuildStack {
    private final List<Implementation<?>> stack;

    public BuildStack() {
        this.stack = new ArrayList<>();
    }
    public BuildStack next(Implementation<?> current) {
        BuildStack next = new BuildStack();
        next.stack.addAll(stack);
        next.stack.add(current);
        return next;
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    public <T> boolean contains(Implementation<T> current) {
        return stack.contains(current);
    }
}

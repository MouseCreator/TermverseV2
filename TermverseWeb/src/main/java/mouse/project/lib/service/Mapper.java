package mouse.project.lib.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class Mapper {

    public static class ListMapperState<T> {
        private final List<T> objects;

        public ListMapperState(Collection<T> objects) {
            this.objects = new ArrayList<>(objects);
        }
        public List<T> get() {
            return new ArrayList<>(objects);
        }
        public <R> ListMapperState<R> to(Function<T, R> transform) {
            List<R> mappedObjects = objects.stream().map(transform).toList();
            return new ListMapperState<>(mappedObjects);
        }
        public <R> List<R> toAndGet(Function<T, R> transform) {
            return to(transform).get();
        }
    }

    public static class SingleMapperState<T> {
        private final T object;

        public SingleMapperState(T object) {
            this.object = object;
        }
        public T get() {
            return object;
        }
        public <R> SingleMapperState<R> to(Function<T, R> transform) {
            R mappedObject = transform.apply(object);
            return new SingleMapperState<>(mappedObject);
        }
        public <R> R toAndGet(Function<T, R> transform) {
            return to(transform).get();
        }
    }
    public static <T> ListMapperState<T> mapAll(Collection<T> objects) {
        if (objects == null) {
            throw new NullPointerException("Unable to map null collection");
        }
        return new ListMapperState<>(objects);
    }

    public static <T> SingleMapperState<T> map(T object) {
        if (object == null) {
            throw new NullPointerException("Unable to map null object");
        }
        return new SingleMapperState<>(object);
    }

    public static <T, R> R transform(T object, Function<T, R> transform) {
        if (object == null) {
            throw new NullPointerException("Unable to map null object");
        }
        return new SingleMapperState<>(object).toAndGet(transform);
    }

}

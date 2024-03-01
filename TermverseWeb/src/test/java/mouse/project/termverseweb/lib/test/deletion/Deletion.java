package mouse.project.termverseweb.lib.test.deletion;

import net.bytebuddy.matcher.StringMatcher;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class Deletion {
    public <ID> IdDeleter<ID> withIdDeletion(Consumer<ID> consumer) {
        return new IdDeleter<>(consumer);
    }

    public <MODEL> ModelDeleter<MODEL> withModelDeletion(Consumer<MODEL> consumer) {
        return new ModelDeleter<>(consumer);
    }

    public interface Deleter {
        Confirmer then();
    }

    public static class IdDeleter<ID> implements Deleter {
        private final Consumer<ID> consumer;
        public IdDeleter(Consumer<ID> consumer) {
            this.consumer = consumer;
        }
        public IdDeleter<ID> deleteIds(Collection<ID> collection) {
            for (ID id : collection) {
                consumer.accept(id);
            }
            return this;
        }
        public IdDeleter<ID> delete(ID id) {
            consumer.accept(id);
            return this;
        }

        public <R> IdDeleter<ID> deleteEntities(List<R> list, Function<R, ID> mapper) {
            Stream<ID> idStream = list.stream()
                    .map(mapper);
            idStream.forEach(consumer);
            return this;
        }

        public <R> IdDeleter<ID> deleteEntity(R entity, Function<R, ID> mapper) {
            ID apply = mapper.apply(entity);
            return delete(apply);
        }


        public Confirmer then() {
            return new Confirmer();
        }
    }

    public static class ModelDeleter<MODEL> implements Deleter {
        private final Consumer<MODEL> consumer;
        public ModelDeleter(Consumer<MODEL> consumer) {
            this.consumer = consumer;
        }
        public ModelDeleter<MODEL> deleteEntities(List<MODEL> list) {
            list.forEach(consumer);
            return this;
        }

        public ModelDeleter<MODEL> deleteEntity(MODEL entity) {
            consumer.accept(entity);
            return this;
        }

        public Confirmer then() {
            return new Confirmer();
        }
    }

    public static class Confirmer {
        public <R> MultiAssert<R> confirmThatAll(Supplier<List<R>> supplier) {
            List<R> supplied = supplier.get();
            return new MultiAssert<>(supplied);
        }

        public <R> SingleAssert<R> confirmThat(Supplier<R> supplier) {
            R r = supplier.get();
            return new SingleAssert<>(r);
        }

        public <R> MultiAssert<R> confirmThatAll(List<R> models) {
            return new MultiAssert<>(models);
        }

        public <R> SingleAssert<R> confirmThat(R model) {
            return new SingleAssert<>(model);
        }
    }
    public interface ConfirmAction<S> {
        void is(Predicate<S> predicate);
        void isNot(Predicate<S> predicate);
        void is(Predicate<S> predicate, String message);
        void isNot(Predicate<S> predicate, String message);
    }
    public static class SingleAssert<R> implements ConfirmAction<R> {
        private final R target;

        public SingleAssert(R target) {
            this.target = target;
        }

        public void doesNotMatchAny(List<R> anyOf) {
            for (R r : anyOf) {
                Assertions.assertNotEquals(r, target);
            }
        }

        public void is(Predicate<R> predicate) {
            Assertions.assertTrue(predicate.test(target));
        }

        public void isNot(Predicate<R> predicate) {
            Assertions.assertFalse(predicate.test(target));
        }

        public void is(Predicate<R> predicate, String message) {
            Assertions.assertTrue(predicate.test(target), message);
        }

        public void isNot(Predicate<R> predicate, String message) {
            Assertions.assertFalse(predicate.test(target), message);
        }

        public void doesNotMatch(R other) {
            Assertions.assertNotEquals(other, target);
        }
    }

    public static class MultiAssert<R> implements ConfirmAction<List<R>> {
        private final List<R> target;

        public MultiAssert(List<R> target) {
            this.target = target;
        }

        public void doesNotContain(List<R> anyOf) {
            for (R r : anyOf) {
                Assertions.assertFalse(target.contains(r),
                        String.format("Did not expect %s to be in %s", r, anyOf));
            }
        }

        public void isExcludedFrom(List<R> other) {
            for (R r : target) {
                Assertions.assertFalse(other.contains(r),
                        String.format("Did not expect %s to be in %s", r, other));
            }
        }
        public void isSubsetOf(List<R> other) {
            Assertions.assertTrue(other.containsAll(target),
                    String.format("Expected %s to be fully in %s", target, other));
        }

        public void is(Predicate<List<R>> predicate) {
            Assertions.assertTrue(predicate.test(target));
        }

        public void is(Predicate<List<R>> predicate, String messageOnFail) {
            Assertions.assertTrue(predicate.test(target), messageOnFail);
        }

        public void isNot(Predicate<List<R>> predicate) {
            Assertions.assertFalse(predicate.test(target));
        }

        public void isNot(Predicate<List<R>> predicate, String messageOnFail) {
            Assertions.assertFalse(predicate.test(target), messageOnFail);
        }
        public void doesNotMatch(List<R> other) {
            Assertions.assertNotEquals(other, target,
                    String.format("Two lists: %s matched, but shouldn't have", other));
        }

        public void hasAll(List<R> models) {
            Assertions.assertTrue(target.containsAll(models),
                    String.format("Expected %s to be fully in %s", models, target));
        }
    }
}

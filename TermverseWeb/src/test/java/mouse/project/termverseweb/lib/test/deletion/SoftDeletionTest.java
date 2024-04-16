package mouse.project.termverseweb.lib.test.deletion;

import mouse.project.lib.ioc.annotation.Auto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@mouse.project.lib.ioc.annotation.Service
public class SoftDeletionTest {
    private final Deletion deletion;
    @Autowired
    @Auto
    public SoftDeletionTest(Deletion deletion) {
        this.deletion = deletion;
    }

    public <MODEL, ID> BeforeSoftDeletion<MODEL, ID> using(Consumer<ID> deleteFunction, Function<MODEL, ID> mapper) {
        return new BeforeSoftDeletion<>(deleteFunction, mapper);
    }

    public class BeforeSoftDeletion<MODEL, ID> {
        private final Consumer<ID> deleteFunction;
        private final Function<MODEL, ID> mapper;
        public BeforeSoftDeletion(Consumer<ID> deleteFunction, Function<MODEL, ID> mapper) {
            this.deleteFunction = deleteFunction;
            this.mapper = mapper;
        }

        public AfterSoftDeletion<MODEL, ID> removeAll(List<MODEL> models) {
            Deletion.Confirmer confirmer = deletion.withIdDeletion(deleteFunction)
                    .deleteEntities(models, mapper).then();
            return new AfterSoftDeletion<>(models, confirmer, mapper);
        }

        public AfterSoftDeletion<MODEL, ID> remove(MODEL model) {
            return removeAll(List.of(model));
        }

        public AfterSoftDeletion<MODEL, ID> passAll(List<MODEL> models) {
            return new AfterSoftDeletion<>(models, new Deletion.Confirmer(), mapper);
        }

        public AfterSoftDeletion<MODEL, ID> pass(MODEL model) {
            return passAll(List.of(model));
        }
    }

    public static class AfterSoftDeletion<MODEL, ID> {
        private final List<MODEL> models;
        private final Deletion.Confirmer confirmer;
        private final Function<MODEL, ID> mapper;

        public AfterSoftDeletion(List<MODEL> models, Deletion.Confirmer confirmer, Function<MODEL, ID> mapper) {
            this.models = models;
            this.confirmer = confirmer;
            this.mapper = mapper;
        }

        public AfterSoftDeletion<MODEL, ID> validateAbsentIn(Supplier<List<MODEL>> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).doesNotContain(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validatePresentIn(Supplier<List<MODEL>>  modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).hasAll(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateContains(Supplier<List<MODEL>>  modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).isSubsetOf(models);
            return this;
        }
        public AfterSoftDeletion<MODEL, ID> validate(Runnable method) {
            method.run();
            return this;
        }
        public AfterSoftDeletion<MODEL, ID> validate(Consumer<MODEL> consumer) {
            for (MODEL model : models) {
                consumer.accept(model);
            }
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateAll(Consumer<List<MODEL>> consumer) {
            consumer.accept(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateDoesntContain(Supplier<List<MODEL>> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).isExcludedFrom(models);
            return this;
        }
        public AfterSoftDeletion<MODEL, ID> restoreWith(Consumer<ID> restoreFunction) {
            models.stream().map(mapper).forEach(restoreFunction);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateThrows(Class<? extends Throwable> ex, Executable o) {
            Assertions.assertThrows(ex, o);
            return this;
        }

        public AfterSoftDeletionByIds<MODEL,ID> byIds() {
            List<ID> ids = models.stream().map(mapper).toList();
            return new AfterSoftDeletionByIds<>(ids, confirmer, mapper, this);
        }

        public AfterSoftDeletion<MODEL, ID> assertTrue(Predicate<MODEL> predicate) {
            for (MODEL model : models) {
                boolean test = predicate.test(model);
                Assertions.assertTrue (test, model + " did not satisfy predicate " + predicate);
            }
            return this;
        }
        public AfterSoftDeletion<MODEL, ID> assertTrue(Supplier<Boolean> supplier) {
            Boolean b = supplier.get();
            Assertions.assertTrue(b);
            return this;
        }
    }

    public static class AfterSoftDeletionByIds<MODEL, ID> {
        private final List<ID> ids;
        private final Deletion.Confirmer confirmer;
        private final Function<MODEL, ID> mapper;

        private final AfterSoftDeletion<MODEL, ID> parent;

        public AfterSoftDeletionByIds(List<ID> ids,
                                      Deletion.Confirmer confirmer,
                                      Function<MODEL, ID> mapper,
                                      AfterSoftDeletion<MODEL, ID> parent) {
            this.ids = ids;
            this.confirmer = confirmer;
            this.mapper = mapper;
            this.parent = parent;
        }

        public AfterSoftDeletion<MODEL, ID> validateAbsentIn(Supplier<List<MODEL>> modelsToTest) {
            List<ID> list = mapByIds(modelsToTest);
            confirmer.confirmThatAll(list).doesNotContain(ids);
            return parent;
        }

        @NotNull
        private List<ID> mapByIds(Supplier<List<MODEL>> modelsToTest) {
            return modelsToTest.get().stream().map(mapper).toList();
        }

        public AfterSoftDeletion<MODEL, ID> validatePresentIn(Supplier<List<MODEL>>  modelsToTest) {
            List<ID> list = mapByIds(modelsToTest);
            confirmer.confirmThatAll(list).hasAll(ids);
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> validateContains(Supplier<List<MODEL>>  modelsToTest) {
            List<ID> list = mapByIds(modelsToTest);
            confirmer.confirmThatAll(list).isSubsetOf(ids);
            return parent;
        }
        public AfterSoftDeletion<MODEL, ID> validate(Runnable method) {
            method.run();
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> assertTrue(Predicate<ID> predicate) {
            for (ID id : ids) {
                boolean test = predicate.test(id);
                Assertions.assertTrue (test, id + " did not satisfy predicate " + predicate);
            }
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> assertTrue(Supplier<Boolean> supplier) {
            Boolean b = supplier.get();
            Assertions.assertTrue(b);
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> validate(Consumer<ID> consumer) {
            for (ID id : ids) {
                consumer.accept(id);
            }
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> validateAll(Consumer<List<ID>> consumer) {
            consumer.accept(ids);
            return parent;
        }

        public AfterSoftDeletion<MODEL, ID> validateDoesntContain(Supplier<List<MODEL>> modelsToTest) {
            List<ID> list = mapByIds(modelsToTest);
            confirmer.confirmThatAll(list).isExcludedFrom(ids);
            return parent;
        }
        public AfterSoftDeletion<MODEL, ID> validateThrows(Class<? extends Throwable> ex, Executable o) {
            Assertions.assertThrows(ex, o);
            return parent;
        }
    }
}

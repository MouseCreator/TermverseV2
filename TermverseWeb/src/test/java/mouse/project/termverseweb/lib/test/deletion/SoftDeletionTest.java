package mouse.project.termverseweb.lib.test.deletion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class SoftDeletionTest {
    private final Deletion deletion;
    @Autowired
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

        public AfterSoftDeletion<MODEL, ID> validateAbsentIn(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).doesNotContain(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validatePresentIn(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).hasAll(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateContains(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).isSubsetOf(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validate(Consumer<List<MODEL>> consumer) {
            consumer.accept(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateDoesntContain(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).isExcludedFrom(models);
            return this;
        }
        public void restoreWith(Consumer<ID> restoreFunction) {
            models.stream().map(mapper).forEach(restoreFunction);
        }

        public AfterSoftDeletion<MODEL, ID> validateThrows(Class<? extends Throwable> ex, Executable o) {
            Assertions.assertThrows(ex, o);
            return this;
        }
    }
}

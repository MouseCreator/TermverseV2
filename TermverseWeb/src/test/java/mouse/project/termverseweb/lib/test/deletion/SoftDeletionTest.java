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

    public <MODEL> BeforeSoftDeletion<MODEL> removeAll(List<MODEL> models) {
        return new BeforeSoftDeletion<>(models);
    }

    public <MODEL> BeforeSoftDeletion<MODEL> remove(MODEL model) {
        return removeAll(List.of(model));
    }

    public class BeforeSoftDeletion<MODEL> {
        private final List<MODEL> models;

        public BeforeSoftDeletion(List<MODEL> models) {
            this.models = models;
        }

        public <ID> AfterSoftDeletion<MODEL, ID> using(Consumer<ID> deleteFunction, Function<MODEL, ID> mapper) {
            Deletion.Confirmer confirmer = deletion.
                    withIdDeletion(deleteFunction).
                    deleteEntities(models, mapper).then();
            return new AfterSoftDeletion<>(models, confirmer, mapper);
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

        public AfterSoftDeletion<MODEL, ID> validateAllAbsentIn(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).doesNotContain(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateAllPresentIn(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).hasAll(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateContainsAll(List<MODEL> modelsToTest) {
            confirmer.confirmThatAll(modelsToTest).isSubsetOf(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validate(Consumer<List<MODEL>> consumer) {
            consumer.accept(models);
            return this;
        }

        public AfterSoftDeletion<MODEL, ID> validateContainsAnyOf(List<MODEL> modelsToTest) {
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

package mouse.project.lib.service;

import mouse.project.lib.service.repository.GenericRepository;
import mouse.project.lib.service.result.OptionalResult;
import mouse.project.lib.service.result.RawResult;
import mouse.project.lib.service.result.RawResultCollection;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class StatefulRepositoryCaller<REPOSITORY extends GenericRepository> {
    private final REPOSITORY repository;

    public StatefulRepositoryCaller(REPOSITORY repository) {
        this.repository = repository;
    }

    public <RESPONSE> RawResult<RESPONSE> single(Function<REPOSITORY, RESPONSE> function) {
        return RawResult.from(function.apply(repository));
    }

    public <RESPONSE> RESPONSE raw(Function<REPOSITORY, RESPONSE> function) {
        return function.apply(repository);
    }

    public <RESPONSE> RawResultCollection<RESPONSE> multi(Function<REPOSITORY,
            Collection<RESPONSE>> function) {
        return RawResultCollection.from(function.apply(repository));
    }

    public void none(Consumer<REPOSITORY> function) {
        function.accept(repository);
    }

    public <RESPONSE> OptionalResult<RESPONSE> optional(
            Function<REPOSITORY,
            Optional<RESPONSE>> function) {
        Optional<RESPONSE> apply = function.apply(repository);
        RESPONSE response = apply.orElse(null);
        return OptionalResult.from(response);
    }
}

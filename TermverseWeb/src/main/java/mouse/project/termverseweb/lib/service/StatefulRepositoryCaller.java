package mouse.project.termverseweb.lib.service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import mouse.project.termverseweb.lib.service.result.RawResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;
import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;

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

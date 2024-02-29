package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;

import mouse.project.termverseweb.mapper.Mapper;
import mouse.project.termverseweb.repository.CustomCrudRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericServiceHelper {

    public static class StatefulServiceHelper<MODEL, ID> {
        private final CustomCrudRepository<MODEL, ID> repository;
        public StatefulServiceHelper(CustomCrudRepository<MODEL, ID> repository) {
            this.repository = repository;

        }
        public <CREATE> RawResult<MODEL> save(CREATE createDTO,
                                                Function<CREATE, MODEL> fromCreate) {
            MODEL model = fromCreate.apply(createDTO);
            MODEL savedModel = repository.save(model);
            return RawResult.from(savedModel);
        }

        public RawResult<MODEL> save(MODEL model) {
            MODEL savedModel = repository.save(model);
            return RawResult.from(savedModel);
        }
        public RawResultCollection<MODEL> findAll() {
            List<MODEL> models = repository.findAll();
            return RawResultCollection.from(models);
        }

        public RawResult<MODEL> findById(ID id) {
            Optional<MODEL> modelOptional = repository.findById(id);
            MODEL model = modelOptional.orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
            return RawResult.from(model);
        }

        public <UPDATE> RawResult<MODEL> update(UPDATE createDTO,
                                                Function<UPDATE, MODEL> fromCreate) {
            MODEL model = fromCreate.apply(createDTO);
            MODEL savedModel = repository.save(model);
            return RawResult.from(savedModel);
        }

        public RawResult<MODEL> update(MODEL model) {
            MODEL savedModel = repository.save(model);
            return RawResult.from(savedModel);
        }
        public void removeById(ID id) {
            repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find model by id: " + id));
            repository.deleteById(id);
        }

    }

    public record RawResult<MODEL>(MODEL result) {

        public static <MODEL> RawResult<MODEL> from(MODEL result) {
            return new RawResult<>(result);
        }

        public <DTO> DTO to(Function<MODEL, DTO> defaultTransformation) {
            return defaultTransformation.apply(result);
        }

        public MODEL getRaw() {
                return result;
            }
        }
    public record OptionalResult<MODEL>(MODEL result) {

        public static <MODEL> OptionalResult<MODEL> from(MODEL result) {
            return new OptionalResult<>(result);
        }

        public <DTO> Optional<DTO> to(Function<MODEL, DTO> defaultTransformation) {
            if (result == null) {
                return Optional.empty();
            }
            return Optional.of(defaultTransformation.apply(result));
        }

        public MODEL getRaw() {
            return result;
        }

        public <DTO> DTO orThrow(Function<MODEL, DTO> defaultTransformation) {
           return orThrow(defaultTransformation, "Entity not found");
        }
        public <DTO> DTO orThrow(Function<MODEL, DTO> defaultTransformation, String exception) {
            return orThrow(defaultTransformation, new EntityNotFoundException(exception));
        }
        public <DTO> DTO orThrow(Function<MODEL, DTO> defaultTransformation, RuntimeException throwable) {
            if (result == null) {
                throw throwable;
            }
            return defaultTransformation.apply(result);
        }
    }

    public record RawResultCollection<MODEL>(Collection<MODEL> result) {

        public static <MODEL> RawResultCollection<MODEL> from(Collection<MODEL> result) {
            return new RawResultCollection<>(result);
        }

        public <DTO> List<DTO> to(Function<MODEL, DTO> defaultTransformation) {
            return Mapper.mapAll(result).toAndGet(defaultTransformation);
        }

        public List<MODEL> getRaw() {
            return new ArrayList<>(result);
        }
    }



    public <MODEL, ID> StatefulServiceHelper<MODEL, ID> with(CustomCrudRepository<MODEL, ID> repository) {
        return new StatefulServiceHelper<>(repository);
    }

    public static class StatefulRepositoryCaller<REPOSITORY extends CustomCrudRepository<MODEL, ID>, MODEL, ID> {
        private final REPOSITORY repository;

        public StatefulRepositoryCaller(REPOSITORY repository) {
            this.repository = repository;
        }

        public  <RESPONSE> RawResult<RESPONSE> single(Function<REPOSITORY, RESPONSE> function) {
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

        public <RESPONSE> OptionalResult<RESPONSE> optional(Function<REPOSITORY, Optional<RESPONSE>> function) {
            Optional<RESPONSE> apply = function.apply(repository);
            RESPONSE response = apply.orElse(null);
            return OptionalResult.from(response);
        }
    }

    public <MODEL, ID, REPOSITORY extends CustomCrudRepository<MODEL, ID>>
    StatefulRepositoryCaller<REPOSITORY, MODEL, ID> use(REPOSITORY repository) {
        return new StatefulRepositoryCaller<>(repository);
    }

}

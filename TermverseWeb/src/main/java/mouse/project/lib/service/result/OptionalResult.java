package mouse.project.lib.service.result;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.function.Function;

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

    public MODEL orThrow() {
        return orThrow("Entity not found");
    }

    public MODEL orThrow(String exception) {
        if (result == null) {
            throw new EntityNotFoundException(exception);
        }
        return result;
    }
}


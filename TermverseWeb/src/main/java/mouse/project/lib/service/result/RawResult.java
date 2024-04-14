package mouse.project.lib.service.result;

import java.util.function.Function;

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

package mouse.project.lib.service.result;

import mouse.project.lib.service.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

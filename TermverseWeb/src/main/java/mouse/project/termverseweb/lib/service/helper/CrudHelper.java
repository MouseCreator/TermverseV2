package mouse.project.termverseweb.lib.service.helper;

import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.result.RawResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;

import java.util.function.Function;

public interface CrudHelper<MODEL, ID> {
    <CREATE> RawResult<MODEL> save(CREATE createDTO, Function<CREATE, MODEL> fromCreate);
    RawResult<MODEL> save(MODEL model);
    RawResultCollection<MODEL> findAll();
    OptionalResult<MODEL> findById(ID id);
    <UPDATE> RawResult<MODEL> update(UPDATE createDTO, Function<UPDATE, MODEL> fromCreate);
    RawResult<MODEL> update(MODEL model);
    void removeById(ID id);
}

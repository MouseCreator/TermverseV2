package mouse.project.termverseweb.lib.service.helper;

import mouse.project.termverseweb.lib.service.result.OptionalResult;
import mouse.project.termverseweb.lib.service.result.RawResult;
import mouse.project.termverseweb.lib.service.result.RawResultCollection;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CrudHelper<MODEL, ID> {
    <CREATE> RawResult<MODEL> save(CREATE createDTO, Function<CREATE, MODEL> fromCreate);
    RawResult<MODEL> save(Supplier<MODEL> fromCreate);
    RawResult<MODEL> save(MODEL model);
    RawResultCollection<MODEL> findAll();
    OptionalResult<MODEL> findById(ID id);
    <UPDATE> RawResult<MODEL> update(UPDATE updateDTO, Function<UPDATE, MODEL> fromUpdate);
    RawResult<MODEL> update(MODEL model);
    void removeById(ID id);
    void safeRemove(ID id);
}

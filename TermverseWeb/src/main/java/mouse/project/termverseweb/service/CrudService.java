package mouse.project.termverseweb.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T save(T model);
    List<T> findAll();
    Optional<T> getById(ID id);
    T updateById(T model, ID id);
    void removeById(ID id);

}

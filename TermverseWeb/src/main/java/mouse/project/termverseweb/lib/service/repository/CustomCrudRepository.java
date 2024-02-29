package mouse.project.termverseweb.lib.service.repository;

import java.util.List;
import java.util.Optional;


public interface CustomCrudRepository<T, ID> extends GenericRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
    T save(T model);
}

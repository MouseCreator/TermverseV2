package mouse.project.lib.service.repository;

import mouse.project.lib.service.model.IdIterable;

import java.util.List;
import java.util.Optional;


public interface CustomCrudRepository<T extends IdIterable<ID>, ID> extends GenericRepository {
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
    T save(T model);
}

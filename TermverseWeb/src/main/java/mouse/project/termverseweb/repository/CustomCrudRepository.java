package mouse.project.termverseweb.repository;

import java.util.List;
import java.util.Optional;


public interface CustomCrudRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
    T save(T model);
}

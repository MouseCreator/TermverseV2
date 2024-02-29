package mouse.project.termverseweb.repository;

import java.util.List;
import java.util.Optional;

public interface SoftDeleteCrudRepository<T, ID> extends CustomCrudRepository<T, ID>{
    List<T> findAllIncludeDeleted();
    void restoreById(ID id);
    Optional<T> findByIdIncludeDeleted(ID id);
}

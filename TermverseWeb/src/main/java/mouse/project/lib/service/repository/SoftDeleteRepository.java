package mouse.project.lib.service.repository;


import java.util.List;
import java.util.Optional;

public interface SoftDeleteRepository<T, ID> extends GenericRepository {
    List<T> findAllIncludeDeleted();
    void restoreById(ID id);
    Optional<T> findByIdIncludeDeleted(ID id);
}

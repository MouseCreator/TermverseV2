package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteCrudRepository;
import mouse.project.termverseweb.model.Tag;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@Primary
public interface TagRepository extends Repository<Tag, Long>, SoftDeleteCrudRepository<Tag, Long> {
    @Transactional
    @Modifying
    Tag save(Tag tag);
    @Query("SELECT t FROM Tag t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Tag> findById(Long id);

    @Query("SELECT t FROM Tag t WHERE t.deletedAt IS NULL")
    List<Tag> findAll();
    @Transactional
    @Modifying
    @Query(value = "UPDATE tags t SET deleted_at = NOW() WHERE t.id = :id", nativeQuery = true)
    void deleteById(Long id);

    @Query("SELECT t from Tag t")
    List<Tag> findAllIncludeDeleted();
    @Transactional
    @Modifying
    @Query(value = "UPDATE tags t SET deleted_at = NULL WHERE t.id = :id", nativeQuery = true)
    void restoreById(Long id);

    @Query(value = "SELECT t FROM Tag t WHERE t.id = :id")
    Optional<Tag> findByIdIncludeDeleted(Long id);
    @Query("SELECT t FROM Tag t " +
            "WHERE t.owner.id = :ownerId " +
            "AND t.deletedAt IS NULL AND t.owner.deletedAt IS NULL")
    List<Tag> getTagsByOwner(Long ownerId);

    @Query("SELECT t FROM Tag t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND t.owner.deletedAt IS NULL " +
            "AND t.owner.id = :ownerId " +
            "AND t.deletedAt IS NULL")
    List<Tag> getTagsByOwnerAndName(Long ownerId, String name);
}

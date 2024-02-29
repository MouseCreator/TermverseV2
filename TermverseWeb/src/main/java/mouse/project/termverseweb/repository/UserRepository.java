package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Long>, SoftDeleteCrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAll();
    @Query("SELECT u FROM User u")
    List<User> findAllIncludeDeleted();
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.deletedAt IS NULL")
    List<User> findAllByNameIgnoreCase(@Param("name") String name);
    @Transactional
    @Modifying
    @Query(value = "UPDATE users u SET deleted_at = NOW() WHERE u.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.deletedAt = NULL WHERE u.id = :id")
    void restoreById(@Param("id") Long id);
    @Transactional
    User save(User user);
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<User> findById(@Param("id") Long id);
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdIncludeDeleted(@Param("id")  Long id);
}

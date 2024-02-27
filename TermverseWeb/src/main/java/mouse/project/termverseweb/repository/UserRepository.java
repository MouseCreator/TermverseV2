package mouse.project.termverseweb.repository;

import mouse.project.termverseweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByNameIgnoreCase(String name);
    @Query("SELECT u FROM User u WHERE u.deleted = true")
    List<User> findAllDeletedUsers();

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdIgnoringDeleted(Long id);
}

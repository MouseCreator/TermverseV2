package mouse.project.termverseweb.repository;

import mouse.project.termverseweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByNameIgnoreCase(String name);
}

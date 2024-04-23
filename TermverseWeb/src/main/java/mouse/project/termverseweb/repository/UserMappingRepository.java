package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.lib.web.annotation.Param;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.model.UserMapping;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserMappingRepository extends Repository<UserMapping, String>, CustomCrudRepository<UserMapping, String> {
    @Query("SELECT m FROM UserMapping m WHERE m.user.deletedAt IS NULL ")
    List<UserMapping> findAll();
    @Query("SELECT m FROM UserMapping m WHERE m.securityId = :id AND m.user.deletedAt IS NULL ")
    Optional<UserMapping> findById(@Param("id") String id);
    @Query("DELETE FROM UserMapping m WHERE m.securityId = :id")
    @Modifying
    void deleteById(@Param("id") String id);
    @Transactional
    @Modifying
    UserMapping save(UserMapping model);
}

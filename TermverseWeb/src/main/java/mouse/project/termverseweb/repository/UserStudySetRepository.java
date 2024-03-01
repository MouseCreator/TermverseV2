package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.model.UserStudySet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStudySetRepository extends Repository<UserStudySet, Long>,
        CustomCrudRepository<UserStudySet, Long> {
    List<UserStudySet> findAll();
    Optional<UserStudySet> findById(@Param("id") Long id);
    void deleteById(@Param("id") Long id);
    @Transactional
    @Modifying
    UserStudySet save(UserStudySet model);
}

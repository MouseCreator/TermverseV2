package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.CustomCrudRepository;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserTerm;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserTermRepository extends Repository<UserTerm, Long>, CustomCrudRepository<UserTerm, Long> {
    List<UserTerm> findAll();
    Optional<UserTerm> findById(@Param("id") Long id);
    void deleteById(@Param("id") Long id);
    @Query("DELETE " +
            "FROM UserTerm ut " +
            "WHERE ut.user = :userId AND ut.term IN (:termIds)")
    void deleteByUserAndTerms(@Param("id") Long id);
    @Transactional
    @Modifying
    UserTerm save(UserTerm model);
    @Query("SELECT ut " +
            "FROM UserTerm ut " +
            "WHERE ut.user = :userId AND ut.term IN (:termIds) AND " +
            "ut.user.deletedAt IS NULL AND ut.term.deletedAt IS NULL ")
    Optional<User> findByUserAndTerms(@Param("user") Long userId, @Param("termIds") List<Long> terms);
}

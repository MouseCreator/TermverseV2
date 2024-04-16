package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.model.UserTerm;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.stereotype.Repository
@Primary
public interface UserTermRepository extends Repository<UserTerm, Long>, GenericRepository {
    @Query("SELECT ut FROM UserTerm ut WHERE ut.user.deletedAt IS NULL AND ut.term.deletedAt IS NULL")
    List<UserTerm> findAll();
    @Modifying
    @Transactional
    @Query("DELETE FROM UserTerm ut WHERE ut.user.id = :userId AND ut.term.id IN (:termIds)" +
            " AND ut.user.deletedAt IS NULL AND ut.term.deletedAt IS NULL")
    void deleteByUserAndTerms(@Param("userId") Long id, @Param("termIds") List<Long> termIds);
    @Transactional
    UserTerm save(UserTerm model);
    @Query("SELECT ut " +
            "FROM UserTerm ut " +
            "WHERE ut.user.id = :userId AND ut.term.id IN (:termIds) AND " +
            "ut.user.deletedAt IS NULL AND ut.term.deletedAt IS NULL ")
    List<UserTerm> findByUserAndTerms(@Param("userId") Long userId, @Param("termIds") List<Long> terms);
}

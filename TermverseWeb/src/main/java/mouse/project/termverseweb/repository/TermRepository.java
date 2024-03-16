package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteCrudRepository;
import mouse.project.termverseweb.model.Term;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface TermRepository extends Repository<Term, Long>,
        SoftDeleteCrudRepository<Term, Long> {
    @Query("SELECT t FROM Term t WHERE t.deletedAt IS NULL")
    List<Term> findAll();

    @Query("SELECT t FROM Term t")
    List<Term> findAllIncludeDeleted();
    @Query("SELECT t FROM Term t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Term> findById(@Param("id") Long id);
    @Query("SELECT t FROM Term t WHERE t.id = :id")
    Optional<Term> findByIdIncludeDeleted(@Param("id")  Long id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE terms t SET deleted_at = NOW() WHERE t.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("UPDATE StudySet t SET t.deletedAt = NULL WHERE t.id = :id")
    void restoreById(@Param("id") Long id);
    @Transactional
    @Modifying
    Term save(Term  model);
    @Query(value = "DELETE FROM study_sets_terms WHERE term_id = :termId", nativeQuery = true)
    @Modifying
    void removeTermFormStudySetsById(Long termId);
}
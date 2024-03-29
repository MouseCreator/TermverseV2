package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.SoftDeleteCrudRepository;
import mouse.project.termverseweb.model.SizedStudySet;
import mouse.project.termverseweb.model.StudySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface StudySetRepository extends Repository<StudySet, Long>, SoftDeleteCrudRepository<StudySet, Long> {
    @Query("SELECT s FROM StudySet s WHERE s.deletedAt IS NULL")
    List<StudySet> findAll();
    @Query("SELECT s FROM StudySet s")
    List<StudySet> findAllIncludeDeleted();
    @Query("SELECT s FROM StudySet s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.deletedAt IS NULL")
    List<StudySet> findAllByNameIgnoreCase(@Param("name") String name);
    @Query("SELECT s " +
            "FROM StudySet s " +
            "WHERE ((s.createdAt BETWEEN :start_date AND :end_date) " +
            "AND s.deletedAt IS NULL)")
    List<StudySet> findAllByCreatedDateRange(@Param("start_date") LocalDateTime startDate,
                                             @Param("end_date") LocalDateTime endDate);
    @Transactional
    @Modifying
    @Query(value = "UPDATE study_sets s SET deleted_at = NOW() WHERE s.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("UPDATE StudySet s SET s.deletedAt = NULL WHERE s.id = :id")
    void restoreById(@Param("id") Long id);
    @Transactional
    @Modifying
    StudySet save(StudySet model);
    @Query("SELECT s FROM StudySet s WHERE s.id = :id AND s.deletedAt IS NULL")
    Optional<StudySet> findById(@Param("id") Long id);
    @Query("SELECT s FROM StudySet s WHERE s.id = :id")
    Optional<StudySet> findByIdIncludeDeleted(@Param("id")  Long id);
    @Query("SELECT s " +
            "FROM StudySet s JOIN s.users u " +
            "WHERE u.id = :userId " +
            "AND s.deletedAt IS NULL AND u.deletedAt IS NULL")
    List<StudySet> findAllByUserId(@Param("userId") Long userId);
    @Query("SELECT s " +
            "FROM StudySet s JOIN s.users u " +
            "WHERE u.id = :userId " +
            "AND s.deletedAt IS NULL AND u.deletedAt IS NULL")
    Page<StudySet> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT s FROM StudySet s " +
            "LEFT JOIN FETCH s.terms t " +
            "WHERE s.id = :id " +
            "AND s.deletedAt IS NULL")
    Optional<StudySet> findAllByIdWithTerms(@Param("id") Long id);
    @Query("SELECT new mouse.project.termverseweb.model.SizedStudySet(s, SIZE(s.terms)) " +
            "FROM StudySet s " +
            "WHERE s.id = :setId " +
            "AND s.deletedAt IS NULL")
    Optional<SizedStudySet> findByIdWithSize(@Param("id") Long id);
    @Query("SELECT SIZE(s.terms) " +
            "FROM StudySet s " +
            "WHERE s.id = :setId " +
            "AND s.deletedAt IS NULL")
    Integer getTermCount(Long id);
}

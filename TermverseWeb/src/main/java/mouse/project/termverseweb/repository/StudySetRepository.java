package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageImpl;

import mouse.project.termverseweb.lib.service.repository.SoftDeleteCrudRepository;
import mouse.project.termverseweb.model.SizedStudySet;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.service.sort.StudySetSorter;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@Primary
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

    default mouse.project.lib.data.page.Page<StudySet> findAllByUserId(Long userId, PageDescription pageDescription) {
        Page<StudySet> p = findAllByUserId(userId, PageRequest.of(pageDescription.number(), pageDescription.size()));
        List<StudySet> studySets = p.stream().toList();
        return new PageImpl<>(studySets, pageDescription);
    }
    @Query("SELECT s FROM StudySet s " +
            "LEFT JOIN FETCH s.terms t " +
            "WHERE s.id = :id " +
            "AND s.deletedAt IS NULL")
    Optional<StudySet> findAllByIdWithTerms(@Param("id") Long id);
    @Query("SELECT new mouse.project.termverseweb.model.SizedStudySet(s, SIZE(s.terms)) " +
            "FROM StudySet s " +
            "WHERE s.id = :setId " +
            "AND s.deletedAt IS NULL")
    Optional<SizedStudySet> findByIdWithSize(@Param("setId") Long id);
    @Query("SELECT SIZE(s.terms) " +
            "FROM StudySet s " +
            "WHERE s.id = :setId " +
            "AND s.deletedAt IS NULL")
    Integer getTermCount(@Param("setId") Long setId);
    @Query("SELECT us from UserStudySet us " +
            "LEFT JOIN FETCH us.studySet s " +
            "LEFT JOIN FETCH us.studySet u " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND us.type = :type " +
            "AND us.studySet.id IN (SELECT uss.studySet.id FROM UserStudySet uss " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND uss.user.id = :userId AND uss.user.deletedAt IS NULL AND uss.studySet.deletedAt IS NULL)" )
    Page<UserStudySet> findAllByNameAndUser(@Param("name") String name, @Param("userId") Long userId,
                                            @Param("type") String type, Pageable pageable, Sort sort);

    default mouse.project.lib.data.page.Page<UserStudySet> findAllByNameAndUser(String name,
                                                                                Long userId,
                                                                                String type,
                                                                                PageDescription pageDescription, String sortBy) {
        Sort orders = StudySetSorter.sortBy(sortBy);
        PageRequest pages = PageRequest.of(pageDescription.number(), pageDescription.size());
        Page<UserStudySet> p = findAllByNameAndUser(name, userId, type, pages, orders);
        List<UserStudySet> studySets = p.stream().toList();
        return new PageImpl<>(studySets, pageDescription);
    }
    @Query("SELECT us from UserStudySet us " +
            "LEFT JOIN FETCH us.studySet s " +
            "LEFT JOIN FETCH us.studySet u " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND us.type = :type " +
            "AND us.user.id = :userId AND s.deletedAt IS NULL AND u.deletedAt IS NULL" )
    Page<UserStudySet> findAllByNameAndType(@Param("name") String query, @Param("type") String type, Pageable page, Sort sort);

    default mouse.project.lib.data.page.Page<UserStudySet> findAllByNameAndType(String name,
                                                                                String type,
                                                                                PageDescription pageDescription,
                                                                                String sortBy) {
        Sort orders = StudySetSorter.sortBy(sortBy);
        PageRequest pages = PageRequest.of(pageDescription.number(), pageDescription.size());
        Page<UserStudySet> p = findAllByNameAndType(name, type, pages, orders);
        List<UserStudySet> studySets = p.stream().toList();
        return new PageImpl<>(studySets, pageDescription);
    }
}

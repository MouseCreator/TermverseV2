package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.data.executor.result.Raw;
import mouse.project.lib.data.sort.SortOrder;
import mouse.project.termverseweb.model.*;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageFactory;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.termverseweb.repository.transform.UserStudySetTransformer;
import mouse.project.termverseweb.service.sort.StudySetSorter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class StudySetRepositoryImpl implements StudySetRepository {
    private final Executor executor;
    private final PageFactory pages;
    private TermRepository termRepository = null;
    private UserStudySetTransformer userStudySetTransformer;
    @Auto
    public StudySetRepositoryImpl(Executor executor, PageFactory pages) {
        this.executor = executor;
        this.pages = pages;
    }
    @After
    public void setTermRepository(TermRepository termRepository) {
        this.termRepository = termRepository;
    }
    @After
    public void setUserStudySetTransformer(UserStudySetTransformer userStudySetTransformer) {
        this.userStudySetTransformer = userStudySetTransformer;
    }

    @Override
    public List<StudySet> findAll() {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM study_sets s WHERE s.deleted_at IS NULL"
        ).list(StudySet.class));
    }

    @Override
    public List<StudySet> findAllIncludeDeleted() {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM study_sets"
        ).list(StudySet.class));
    }

    @Override
    public List<StudySet> findAllByNameIgnoreCase(String name) {
        return executor.read(e -> e.executeQuery(
                    "SELECT * FROM study_sets s " +
                        "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?, '%')) " +
                        "AND s.deleted_at IS NULL", name).list(StudySet.class)
        );
    }

    @Override
    public List<StudySet> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return executor.read(e -> e.executeQuery(
                "SELECT * " +
                    "FROM study_sets s " +
                    "WHERE ((s.created_at BETWEEN ? AND ?) " +
                    "AND s.deleted_at IS NULL)", startDate, endDate
        ).list(StudySet.class));
    }

    @Override
    public void deleteById(Long id) {
        executor.write(e -> e.execute("UPDATE study_sets s SET deleted_at = NOW() WHERE s.id = ?", id));
    }

    @Override
    public void restoreById(Long id) {
        executor.write(e -> e.execute("UPDATE study_sets s SET deleted_at = NULL WHERE s.id = ?", id));
    }

    @Override
    public StudySet save(StudySet model) {
        if (model.getId() == null) {
            executor.write(e -> e.execute(
                            "INSERT INTO study_sets " +
                                    "(name, picture_url, created_at, deleted_at)" +
                                    " VALUES (?, ?, ?, ?)",
                            model.getName(), model.getPictureUrl(), model.getCreatedAt(), null)
                    .affectOne().singleKey(Long.class, model::setId));
        } else {
            executor.write(e -> e.execute(
                            "UPDATE study_sets SET " +
                                    "name = ?, picture_url = ?, created_at = ?, deleted_at = ? " +
                                    "WHERE id = ?",
                            model.getName(), model.getPictureUrl(), model.getCreatedAt(), null, model.getId())
                    .affectOne());
        }
        return model;
    }

    @Override
    public Optional<StudySet> findById(Long id) {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM study_sets s WHERE s.id = ? AND s.deleted_at IS NULL", id)
                .optional(StudySet.class));
    }

    @Override
    public Optional<StudySet> findByIdIncludeDeleted(Long id) {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM study_sets s WHERE s.id = ?"
                , id).optional(StudySet.class));
    }

    @Override
    public List<StudySet> findAllByUserId(Long userId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * " +
                    "FROM study_sets s " +
                    "INNER JOIN users_study_sets us ON s.id = us.study_set_id " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "WHERE u.id = ? " +
                    "AND s.deleted_at IS NULL AND u.deleted_at IS NULL", userId
        ).list(StudySet.class));
    }

    @Override
    public org.springframework.data.domain.Page<StudySet> findAllByUserId(Long userId, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<StudySet> findAllByUserId(Long userId, PageDescription pageDescription) {
        int limit = pageDescription.size();
        int offset = pageDescription.number() * pageDescription.size();
        List<StudySet> list = executor.read(e -> e.executeQuery(
                "SELECT * " +
                        "FROM study_sets s " +
                        "INNER JOIN users_study_sets us ON s.id = us.study_set_id " +
                        "INNER JOIN users u ON u.id = us.user_id " +
                        "WHERE u.id = ? " +
                        "AND s.deleted_at IS NULL AND u.deleted_at IS NULL " +
                        "LIMIT ? OFFSET ?",
                userId, limit, offset
        ).list(StudySet.class));
        return pages.pageOf(list, pageDescription);
    }

    @Override
    public Optional<StudySet> findAllByIdWithTerms(Long id) {
        Optional<StudySet> studySetOptional = findById(id);
        if (studySetOptional.isEmpty()) {
            return studySetOptional;
        }
        StudySet studySet = studySetOptional.get();
        List<Term> allByStudySet = termRepository.findAllByStudySet(id);
        studySet.setTerms(allByStudySet);
        return Optional.of(studySet);
    }

    @Override
    public Integer getTermCount(Long setId) {
        return executor.read(e ->
            e.executeQuery(
                    "SELECT COUNT(DISTINCT t.id) " +
                            "FROM terms t " +
                            "INNER JOIN study_sets_terms st ON t.id = st.term_id " +
                            "INNER JOIN study_sets s ON s.id = st.set_id " +
                            "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL", setId
            ).getRaw().map(Raw::getInt));
    }

    @Override
    public org.springframework.data.domain.Page<UserStudySet> findAllByNameAndUser(String name, Long userId, String type, Pageable pageable, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<UserStudySet> findAllByNameAndUser(String name, Long userId, String type, PageDescription pageDescription, String sortBy) {
        List<UserStudySet> list = executor.read(e ->
                e.executeQuery(
                        "SELECT u.*, s.*, us.type " +
                                "FROM users_study_sets us " +
                                "JOIN users u ON us.user_id = u.id " +
                                "JOIN study_sets s ON us.study_set_id = s.id " +
                                "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?, '%'))" +
                                "  AND us.type = ?" +
                                "  AND s.deleted_at IS NULL" +
                                "  AND u.deleted_at IS NULL" +
                                "  AND s.id IN " +
                                        "(SELECT uss.study_set_id " +
                                        "FROM users_study_sets uss " +
                                        "JOIN users uu ON uss.user_id = uu.id " +
                                        "JOIN study_sets ss ON uss.study_set_id = ss.id " +
                                        "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?, '%')) " +
                                        "AND uu.id = ? " +
                                        "AND ss.deleted_at IS NULL " +
                                        "AND uu.deleted_at IS NULL " +
                                ")"
                        , name, type, name, userId).adjustedList(UserStudySetModelFull.class).map(userStudySetTransformer::transform)
                        .get());
        return pages.pageOf(list, pageDescription);
    }

    @Override
    public org.springframework.data.domain.Page<UserStudySet> findAllByNameAndType(String query, String type, Pageable page, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<UserStudySet> findAllByNameAndType(String name, String type, PageDescription pageDescription, String sortBy) {
        List<UserStudySet> list = executor.read(e ->
                e.executeQuery(
                                "SELECT u.*, s.*, us.type " +
                                        "FROM users_study_sets us " +
                                        "JOIN users u ON us.user_id = u.id " +
                                        "JOIN study_sets s ON us.study_set_id = s.id " +
                                        "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?, '%'))" +
                                        "  AND us.type = ?" +
                                        "  AND s.deleted_at IS NULL" +
                                        "  AND u.deleted_at IS NULL"
                                , name, type).adjustedList(UserStudySetModelFull.class).map(userStudySetTransformer::transform)
                        .get());
        SortOrder<UserStudySet> sortOrder = StudySetSorter.chooseSortOrder(sortBy);
        return pages.applyPageDescription(list, pageDescription, sortOrder);
    }


    @Override
    public Optional<SizedStudySet> findByIdWithSize(Long id) {
        Optional<StudySet> studySet = findById(id);
        if (studySet.isEmpty()) {
            return Optional.empty();
        }
        Integer termCount = getTermCount(id);
        return Optional.of(new SizedStudySet(studySet.get(), termCount));
    }
}

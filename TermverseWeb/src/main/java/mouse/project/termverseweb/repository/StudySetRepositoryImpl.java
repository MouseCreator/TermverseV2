package mouse.project.termverseweb.repository;

import mouse.project.lib.data.exception.ExecutorException;
import mouse.project.lib.data.executor.Executor;
import mouse.project.termverseweb.model.SizedStudySet;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.lib.data.exception.DaoException;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageFactory;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class StudySetRepositoryImpl implements StudySetRepository {
    private final Executor executor;
    private final PageFactory pages;
    private TermRepository termRepository = null;
    @Auto
    public StudySetRepositoryImpl(Executor executor, PageFactory pages) {
        this.executor = executor;
        this.pages = pages;
    }
    @After
    public void setTermRepository(TermRepository termRepository) {
        this.termRepository = termRepository;
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
        executor.write(e -> e.execute(
            "INSERT INTO study_sets " +
                "(name, picture_url, created_at, deleted_at)" +
                " VALUES (?, ?, ?, ?)",
                        model.getName(), model.getPictureUrl(), model.getCreatedAt(), null)
                .affectOne().singleKey(Long.class, model::setId));
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
        return executor.read(e -> {
            try {
                return e.executeQuery(
                        "SELECT COUNT(*) " +
                            "FROM terms t " +
                            "INNER JOIN study_sets_terms st ON t.id = st.term_id " +
                            "INNER JOIN study_sets s ON s.id = st.set_id " +
                            "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL", setId
                ).getRaw().getInt(1);
            } catch (SQLException ex) {
                throw new DaoException(ex);
            }
        });
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

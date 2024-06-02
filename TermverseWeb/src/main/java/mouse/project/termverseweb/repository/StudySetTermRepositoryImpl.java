package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.data.executor.result.Raw;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.SetTermModel;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class StudySetTermRepositoryImpl implements StudySetTermRepository {

    private final Executor executor;
    private StudySetRepository setRepository = null;
    private TermRepository termRepository = null;
    @After
    public void setStudySetRepository(StudySetRepository studySetRepository) {
        this.setRepository = studySetRepository;
    }
    @After
    public void setTermsRepository(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Auto
    public StudySetTermRepositoryImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public SetTerm save(SetTerm st) {
        Long setId = st.getSet().getId();
        Long termId = st.getTerm().getId();
        executor.write(e -> e.execute(
                "INSERT INTO study_sets_terms (set_id, term_id) VALUES (?, ?)", setId, termId
        )).affectOne();
        return st;
    }

    @Override
    public Optional<SetTerm> findById(Long termId, Long setId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM study_sets_terms st " +
                    "INNER JOIN study_sets s ON st.set_id = s.id "  +
                    "INNER JOIN terms t ON st.term_id = t.id "  +
                    "WHERE st.term_id = ? AND st.set_id = ? " +
                    "AND t.deleted_at IS NULL AND s.deleted_at IS NULL", termId, setId)
                .optional(SetTermModel.class).map(this::mapper));
    }

    private SetTerm mapper (SetTermModel model) {
        SetTerm setTerm = new SetTerm();
        StudySet studySet = model.getStudySet();
        Term term = model.getTerm();
        setTerm.setSet(studySet);
        setTerm.setTerm(term);
        return setTerm;
    }

    @Override
    public List<SetTerm> findAll() {
        return executor.read(e -> e.executeQuery(
                            "SELECT * FROM study_sets_terms st " +
                                "INNER JOIN study_sets s ON st.set_id = s.id "  +
                                "INNER JOIN terms t ON st.term_id = t.id "  +
                                "WHERE t.deleted_at IS NULL AND s.deleted_at IS NULL")
                .adjustedList(SetTermModel.class)
                .map(this::mapper).get());
    }

    @Override
    public List<Term> getTermsFromStudySet(Long setId) {
        return executor.read(e -> e.executeQuery(
                    "SELECT t.* FROM terms t " +
                        "INNER JOIN study_sets_terms st ON st.term_id = t.id " +
                        "INNER JOIN study_sets s ON st.set_id = s.id "  +
                        "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL", setId
        ).list(Term.class));
    }

    @Override
    public int getTermCount(Long setId) {
        return executor.read(e -> e.executeQuery(
                "SELECT COUNT (t.id) FROM terms t " +
                        "INNER JOIN study_sets_terms st ON st.term_id = t.id " +
                        "INNER JOIN study_sets s ON s.id = st.set_id " +
                        "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL", setId
        ).getRaw().map(Raw::getInt));
    }

    @Override
    public void delete(Long termId, Long setId) {
        executor.write(e -> e.execute("DELETE FROM study_sets_terms st WHERE st.term_id = ? AND st.set_id = ?",
                termId, setId));
    }
}

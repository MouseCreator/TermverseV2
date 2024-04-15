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
                    "WHERE st.term_id = ? AND st.set_id = ? " +
                    "AND st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL", termId, setId)
                .optional(SetTermModel.class).map(this::mapper));
    }

    private SetTerm mapper (SetTermModel model) {
        SetTerm setTerm = new SetTerm();

        Long setId = model.getSetId();
        Optional<StudySet> setById = setRepository.findById(setId);
        setById.ifPresent(setTerm::setSet);

        Long termId = model.getTermId();
        Optional<Term> termById = termRepository.findById(termId);
        termById.ifPresent(setTerm::setTerm);

        return setTerm;
    }

    @Override
    public List<SetTerm> findAll() {
        return executor.read(e -> e.executeQuery(
                            "SELECT * FROM study_sets_terms st " +
                                "WHERE st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL")
                .adjustedList(SetTermModel.class).map(this::mapper).get());
    }

    @Override
    public List<Term> getTermsFromStudySet(Long setId) {
        return executor.read(e -> e.executeQuery(
                    "SELECT * FROM terms t " +
                        "INNER JOIN study_sets_terms st " +
                        "INNER JOIN study_sets s " +
                        "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL"
        ).list(Term.class));
    }

    @Override
    public int getTermCount(Long setId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM terms t " +
                        "INNER JOIN study_sets_terms st " +
                        "INNER JOIN study_sets s " +
                        "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL"
        ).getRaw().map(Raw::getInt));
    }

    @Override
    public void delete(Long termId, Long setId) {
        executor.write(e -> e.execute("DELETE FROM study_sets_terms st WHERE st.term_id = ? AND st.set_id = ?",
                termId, setId));
    }
}

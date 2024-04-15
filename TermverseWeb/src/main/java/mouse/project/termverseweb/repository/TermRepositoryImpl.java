package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.termverseweb.model.Term;
import mouse.project.lib.data.utils.DaoUtils;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class TermRepositoryImpl implements TermRepository {
    private final Executor executor;
    private final DaoUtils daoUtils;
    @Auto
    public TermRepositoryImpl(Executor executor, DaoUtils daoUtils) {
        this.executor = executor;
        this.daoUtils = daoUtils;
    }

    @Override
    public List<Term> findAll() {
        return executor.read(e -> e.executeQuery("SELECT * FROM terms t WHERE t.deleted_at IS NULL").list(Term.class));
    }

    @Override
    public List<Term> findAllIncludeDeleted() {
        return executor.read(e -> e.executeQuery("SELECT * FROM terms").list(Term.class));
    }

    @Override
    public List<Term> findAllByStudySet(Long setId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM terms t " +
                        "INNER JOIN study_sets_terms st ON t.id = st.term_id " +
                        "INNER JOIN study_sets s ON s.id = st.set_id " +
                        "WHERE s.id = ? AND s.deleted_at IS NULL AND t.deleted_at IS NULL", setId).list(Term.class));
    }

    @Override
    public Optional<Term> findById(Long id) {
        return executor.read(e -> e.executeQuery("SELECT * FROM Term t WHERE t.id = ? AND t.deleted_at IS NULL", id)
                .optional(Term.class));
    }

    @Override
    public Optional<Term> findByIdIncludeDeleted(Long id) {
        return executor.read(e -> e.executeQuery("SELECT * FROM Term t WHERE t.id = ?", id)
                .optional(Term.class));
    }

    @Override
    public void deleteById(Long id) {
        executor.write(e -> e.execute("UPDATE terms t SET deleted_at = NOW() WHERE t.id = ?", id));
    }

    @Override
    public void restoreById(Long id) {
        executor.write(e -> e.execute("UPDATE terms t SET deleted_at = NULL WHERE t.id = ?", id));
    }

    @Override
    public Term save(Term model) {
        executor.write(e -> e.execute("INSERT INTO terms (term, definition, hint, picture_url, term_order, deleted_at)" +
                                " VALUES (?, ?, ?, ?, ?, ?)",
                model.getTerm(), model.getDefinition(), model.getHint(), model.getPicture_url(), model.getOrder(), null)
                .affectOne().singleKey(Long.class, model::setId));
        return model;
    }

    @Override
    public void removeTermFormStudySetsById(Long termId) {
        executor.write(e -> e.execute("DELETE FROM study_sets_terms WHERE term_id = ?", termId));
    }

    @Override
    public List<Term> findAllByIds(List<Long> termIds) {
        String qm = daoUtils.qMarksList(termIds);
        String sql = String.format(
                "SELECT * FROM terms t WHERE t.id IN %s AND t.deleted_at IS NULL", qm
        );
        return executor.read(e -> e.executeQuery(sql, termIds).list(Term.class));
    }

}

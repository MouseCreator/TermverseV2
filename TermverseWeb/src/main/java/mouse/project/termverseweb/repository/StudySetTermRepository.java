package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.Term;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudySetTermRepository extends Repository<SetTerm, Long>, GenericRepository {
    SetTerm save(SetTerm setTag);
    @Query("SELECT st " +
            "FROM SetTerm st " +
            "WHERE st.term.id = :termId AND st.set.id = :setId " +
            "AND st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL")
    Optional<SetTerm> findById(@Param("termId") Long termId,@Param("setId") Long setId);

    @Query("SELECT st FROM SetTerm st WHERE st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL")
    List<SetTerm> findAll();
    @Query("SELECT st.term " +
            "FROM SetTerm st " +
            "WHERE st.set = :setId " +
            "AND st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL")
    List<Term> getTermsFromStudySet(@Param("setId") Long setId);

    @Query("SELECT COUNT(st) " +
            "FROM SetTerm st " +
            "WHERE st.set.id = :setId " +
            "AND st.term.deletedAt IS NULL AND st.set.deletedAt IS NULL")
    int getTermCount(@Param("setId") Long setId);
    @Query("DELETE FROM SetTerm st WHERE st.term.id = :termId AND st.set.id = :setId")
    @Modifying
    @Transactional
    void delete(@Param("termId") Long termId, @Param("setId") Long setId);
}

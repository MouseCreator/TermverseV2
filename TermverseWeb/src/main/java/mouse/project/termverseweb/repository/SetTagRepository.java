package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.model.SetTag;
import mouse.project.termverseweb.model.StudySet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface SetTagRepository extends Repository<SetTag, Long>, GenericRepository {
    @Query("SELECT st FROM SetTag st")
    List<SetTag> getAll();
    @Modifying
    @Transactional
    SetTag save(SetTag setTag);
    @Query("SELECT st FROM SetTag st " +
            "WHERE st.user.id = :userId AND st.studySet.id = :setId AND st.tag.id = :tagId AND " +
            "st.tag.deletedAt IS NULL AND st.user.deletedAt IS NULL AND st.studySet.deletedAt IS NULL")
    Optional<SetTag> getSetTagById(@Param("userId") Long userId, @Param("setId") Long setId, @Param("tagId") Long tagId);

    @Query("SELECT st.studySet " +
            "FROM SetTag st " +
            "WHERE st.user.id = :userId AND " +
            "st.tag.deletedAt IS NULL AND st.user.deletedAt IS NULL AND st.studySet.deletedAt IS NULL " +
            "AND NOT EXISTS ( " +
                "SELECT t " +
                "FROM Tag t " +
                "WHERE t.id IN :tagIds AND t.id NOT IN (" +
                    "SELECT r.tag.id " +
                    "FROM SetTag r " +
                    "WHERE r.studySet.id = st.studySet.id))"
    )
    List<StudySet> getStudySetsByUserAndTags(@Param("userId") Long userId, @Param("tagIds") List<Long> tagIds);
    @Modifying
    @Transactional
    @Query("DELETE FROM SetTag st WHERE st.user.id = :userId AND st.studySet.id = :setId AND st.tag = :tagId")
    void delete(Long userId, Long setId, Long tagId);

}

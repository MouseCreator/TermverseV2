package mouse.project.termverseweb.repository;

import jakarta.transaction.Transactional;
import mouse.project.termverseweb.lib.service.repository.GenericRepository;
import mouse.project.termverseweb.model.UserStudySet;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
@org.springframework.stereotype.Repository
@Primary
public interface UserStudySetRepository extends Repository<UserStudySet, Long>, GenericRepository {

    @Query("SELECT us FROM UserStudySet us WHERE us.studySet.deletedAt IS NULL AND us.user.deletedAt IS NULL")
    List<UserStudySet> findAll();
    @Query("SELECT us " +
            "FROM UserStudySet us " +
            "WHERE us.user.id = :userId AND us.studySet.id = :setId " +
            "AND us.user.deletedAt IS NULL ANd us.studySet.deletedAt IS NULL")
    Optional<UserStudySet> findByUserAndStudySet(@Param("userId") Long user, @Param("setId") Long studySetId);
    @Transactional
    @Modifying
    @Query("DELETE FROM UserStudySet us WHERE us.user.id = :userId AND us.studySet.id = :setId " +
            "AND us.user.deletedAt IS NULL AND us.studySet.deletedAt IS NULL")
    void deleteByUserAndStudySet(@Param("userId") Long userId, @Param("setId") Long setId);
    @Transactional
    @Modifying
    UserStudySet save(UserStudySet model);
    @Query("SELECT us " +
            "FROM UserStudySet us " +
            "WHERE us.user.id = :userId AND us.type = :type " +
            "AND us.user.deletedAt IS NULL")
    List<UserStudySet> findByUserAndType(@Param("userId") Long userId, @Param("type") String type);

    @Query("SELECT us " +
            "FROM UserStudySet us " +
            "WHERE us.studySet.id = :setId AND us.type = :type " +
            "AND us.user.deletedAt IS NULL")
    List<UserStudySet> findByStudySetAndType(@Param("setId") Long userId, @Param("type") String type);

    @Query("SELECT us " +
            "FROM UserStudySet us " +
            "WHERE us.user.id = :userId " +
            "AND us.user.deletedAt IS NULL")
    List<UserStudySet> findByUser(@Param("userId") Long userId);

}

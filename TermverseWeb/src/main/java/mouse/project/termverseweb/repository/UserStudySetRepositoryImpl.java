package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.model.*;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.termverseweb.repository.transform.UserStudySetTransformer;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class UserStudySetRepositoryImpl implements UserStudySetRepository {
    private final Executor executor;
    private final UserStudySetTransformer transformer;


    @Auto
    public UserStudySetRepositoryImpl(Executor executor, UserStudySetTransformer transformer) {
        this.executor = executor;
        this.transformer = transformer;
    }

    @Override
    public List<UserStudySet> findAll() {
        return executor.read(e -> e.executeQuery(
                "SELECT u.*, s.*, us.type " + "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE s.deleted_at IS NULL AND u.deleted_at IS NULL"
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }

    @Override
    public Optional<UserStudySet> findByUserAndStudySet(Long user, Long studySetId) {
        return executor.read(e -> e.executeQuery(
                "SELECT u.*, s.*, us.type " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE u.id = ? AND s.id = ? " +
                    "AND u.deleted_at IS NULL ANd s.deleted_at IS NULL", user, studySetId
        ).adjustedOptional(UserStudySetModel.class).map(this::transform).get());
    }

    private UserStudySet transform(UserStudySetModel model) {
       return transformer.transform(model);
    }

    @Override
    public void deleteByUserAndStudySet(Long userId, Long setId) {
        executor.write( e -> e.execute(
                "DELETE FROM users_study_sets us WHERE us.user_id = ? AND us.study_set_id = ?", userId, setId
        ));
    }

    @Override
    public UserStudySet save(UserStudySet model) {
        Long userId = model.getUser().getId();
        Long setId = model.getStudySet().getId();
        String type = model.getType();
        executor.write(e -> e.execute("INSERT INTO users_study_sets (user_id, study_set_id, type) VALUES (?,?,?)",
                userId, setId, type));
        return model;
    }

    @Override
    public List<UserStudySet> findByUserAndType(Long userId, String type) {
        return executor.read(e -> e.executeQuery(
                "SELECT u.*, s.*, us.type " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE u.id = ? AND us.type = ? " +
                    "AND u.deleted_at IS NULL AND s.deleted_at IS NULL", userId, type
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }

    @Override
    public List<UserStudySet> findByStudySetAndType(Long setId, String type) {
        return executor.read(e -> e.executeQuery(
                "SELECT u.*, s.*, us.type " +
                        "FROM users_study_sets us " +
                        "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                        "INNER JOIN users u ON u.id = us.user_id " +
                        "WHERE s.id = ? AND us.type = ? " +
                        "AND s.deleted_at IS NULL AND u.deleted_at IS NULL", setId, type
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }

    @Override
    public List<UserStudySet> findByUser(Long userId) {
        return executor.read(e -> e.executeQuery(
                "SELECT u.*, s.*, us.type " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE u.id = ? " +
                    "AND u.deleted_at IS NULL AND s.deleted_at IS NULL", userId
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }
}

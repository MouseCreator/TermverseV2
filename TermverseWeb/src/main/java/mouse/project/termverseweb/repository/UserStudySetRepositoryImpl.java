package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.model.UserStudySetModel;
import mouse.project.lib.ioc.annotation.After;
import mouse.project.lib.ioc.annotation.Dao;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class UserStudySetRepositoryImpl implements UserStudySetRepository {
    private final Executor executor;
    private UserRepository userRepository = null;
    private StudySetRepository studySetRepository = null;
    @Auto
    public UserStudySetRepositoryImpl(Executor executor) {
        this.executor = executor;
    }
    @After
    public void setRepositories(UserRepository userRepository, StudySetRepository studySetRepository) {
        this.userRepository = userRepository;
        this.studySetRepository = studySetRepository;
    }

    @Override
    public List<UserStudySet> findAll() {
        return executor.read(e -> e.executeQuery(
                "SELECT * FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE s.deleted_at IS NULL AND u.deleted_at IS NULL"
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }

    @Override
    public Optional<UserStudySet> findByUserAndStudySet(Long user, Long studySetId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "INNER JOIN study_sets s ON s.id = us.study_set_id " +
                    "WHERE u.id = ? AND s.id = ? " +
                    "AND u.deleted_at IS NULL ANd s.deleted_at IS NULL", user, studySetId
        ).adjustedOptional(UserStudySetModel.class).map(this::transform).get());
    }

    private UserStudySet transform(UserStudySetModel model) {
        UserStudySet userStudySet = new UserStudySet();

        userStudySet.setType(model.getType());

        Long userId = model.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(userStudySet::setUser);

        Long setId = model.getSetId();
        Optional<StudySet> studySetOptional = studySetRepository.findById(setId);
        studySetOptional.ifPresent(userStudySet::setStudySet);

        return userStudySet;
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
                "SELECT * " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "WHERE u.id = ? AND us.type = ? " +
                    "AND u.deleted_at IS NULL", userId, type
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }

    @Override
    public List<UserStudySet> findByUser(Long userId) {
        return executor.read(e -> e.executeQuery(
                "SELECT * " +
                    "FROM users_study_sets us " +
                    "INNER JOIN users u ON u.id = us.user_id " +
                    "WHERE u.id = ? " +
                    "AND u.deleted_at IS NULL", userId
        ).adjustedList(UserStudySetModel.class).map(this::transform).get());
    }
}

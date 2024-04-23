package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.termverseweb.exception.EntityStateException;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
import mouse.project.termverseweb.model.UserMappingModel;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class UserMappingRepositoryImpl implements UserMappingRepository {

    private final Executor executor;
    private UserRepository userRepository;
    @Auto
    public UserMappingRepositoryImpl(Executor executor) {
        this.executor = executor;
    }
    @Auto
    public void setUserRepository(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public List<UserMapping> findAll() {
        return executor.read(e -> e.executeQuery("SELECT * FROM user_mapping m " +
                        "INNER JOIN users u ON u.id = m.user_id " +
                        "WHERE u.deleted_at IS NULL ").
                adjustedList(UserMappingModel.class).map(this::fromModel).get());
    }

    private UserMapping fromModel(UserMappingModel model) {
        Optional<User> userOptional = userRepository.findById(model.getUserId());
        if (userOptional.isEmpty()) {
            throw new EntityStateException("No userOptional found by id: " + model.getUserId());
        }
        User user = userOptional.get();
        return new UserMapping(model.getSecurityId(), user);
    }

    @Override
    public Optional<UserMapping> findById(String id) {
        return executor.read(e -> e.executeQuery("SELECT * FROM user_mapping m " +
                        "INNER JOIN users u ON u.id = m.user_id " +
                        "WHERE m.security_id = ? AND u.deleted_at IS NULL ", id).
                adjustedOptional(UserMappingModel.class).map(this::fromModel).get());
    }

    @Override
    public void deleteById(String id) {
        executor.write (e -> e.execute(
                "DELETE FROM user_mapping m WHERE m.security_id = ?",
                id));
    }

    @Override
    public UserMapping save(UserMapping model) {
        executor.write(e -> e.execute(
                "INSERT INTO user_mapping (security_id, user_id) VALUES (?, ?)",
                model.getSecurityId(),
                model.getUser().getId()
        ));
        return model;
    }
}

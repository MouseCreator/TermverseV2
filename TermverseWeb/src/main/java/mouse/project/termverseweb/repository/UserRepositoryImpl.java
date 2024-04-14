package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.context.ExecutorContext;
import mouse.project.termverseweb.model.User;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class UserRepositoryImpl implements UserRepository {
    private final ExecutorContext executor;
    @Auto
    public UserRepositoryImpl(ExecutorContext executor) {
        this.executor = executor;
    }

    @Override
    public List<User> findAll() {
        return executor.read(e -> e.executeQuery("SELECT * FROM users u WHERE u.deleted_at IS NULL")
                .list(User.class));
    }

    @Override
    public Optional<User> findById(Long id) {
        return executor.read(e -> e.executeQuery( "SELECT * FROM users u WHERE u.id = ? AND u.deleted_at IS NULL", id)
                .optional(User.class));
    }

    @Override
    public void deleteById(Long id) {
        executor.write(e -> e.execute("UPDATE users u SET deleted_at = NOW() WHERE u.id = ?", id));
    }

    @Override
    public User save(User model) {
        executor.write(e -> e.execute("INSERT INTO users (name, picture_url, deleted_at) VALUES (?, ?, ?)",
                model.getName(), model.getProfilePictureUrl(), null).singleKey(Long.class, model::setId));
        return model;
    }

    @Override
    public List<User> findAllIncludeDeleted() {
        return executor.read(e -> e.executeQuery("SELECT * FROM users").list(User.class));
    }

    @Override
    public void restoreById(Long id) {
        executor.write(e -> e.execute("UPDATE users u SET deleted_at = NULL WHERE u.id = ?", id));
    }

    @Override
    public Optional<User> findByIdIncludeDeleted(Long id) {
        return executor.read(e -> e.executeQuery( "SELECT * FROM users u WHERE u.id = ?", id)
                .optional(User.class));
    }

    @Override
    public List<User> findAllByNameIgnoreCase(String name) {
        return executor.read(e -> e.executeQuery(
                    "SELECT u FROM users u " +
                        "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', ?, '%')) " +
                        "AND u.deletedAt IS NULL")
                .list(User.class));
    }
}

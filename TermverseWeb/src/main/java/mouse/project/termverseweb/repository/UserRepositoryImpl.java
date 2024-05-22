package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.data.executor.result.Raw;
import mouse.project.termverseweb.model.User;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class UserRepositoryImpl implements UserRepository {
    private final Executor executor;
    @Auto
    public UserRepositoryImpl(Executor executor) {
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
        if (model.getId() == null) {
            executor.write(e -> e.execute(
                            "INSERT INTO users (name, profile_picture_url, deleted_at) VALUES (?, ?, ?)",
                            model.getName(), model.getProfilePictureUrl(), null)
                    .affectOne().singleKey(Long.class, model::setId));
        } else {
            executor.write(e -> e.execute(
                            "UPDATE users SET name = ?, profile_picture_url = ?, deleted_at = ? WHERE id = ?",
                            model.getName(), model.getProfilePictureUrl(), null, model.getId())
                    .affectOne());
        }
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
    public boolean existsByName(String login) {
        return executor.read(e ->
                e.executeQuery("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM users u WHERE u.name = ? AND deleted_at IS NULL", login)
                .getRaw().map(Raw::getBoolean));
    }

    @Override
    public List<User> findAllByNameIgnoreCase(String name) {
        return executor.read(e -> e.executeQuery(
                    "SELECT * FROM users u " +
                        "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', ?, '%')) " +
                        "AND u.deleted_at IS NULL", name)
                .list(User.class));
    }
}

package mouse.project.termverseweb.repository;

import mouse.project.lib.data.executor.Executor;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Dao;
import mouse.project.termverseweb.model.Tag;
import mouse.project.termverseweb.model.User;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
@Dao
@NoRepositoryBean
public class TagRepositoryImpl implements TagRepository {
    private final Executor executor;
    @Auto
    public TagRepositoryImpl(Executor executor) {
        this.executor = executor;
    }

    @Override
    public List<Tag> findAll() {
        return executor.read(e -> e.executeQuery("SELECT * FROM tags t WHERE t.deleted_at IS NULL").
                adjustedList(Tag.class).apply(this::addUser).get());
    }

    private void addUser(Tag tag) {
        Long id = tag.getId();
        Optional<User> optional = executor.read(e -> e.executeQuery(
                "SELECT * " +
                    "FROM users u " +
                    "INNER JOIN tags t ON u.id = t.owner " +
                    "WHERE t.id = ?", id
        ).optional(User.class));
        optional.ifPresent(tag::setOwner);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return executor.read(e -> e.executeQuery("SELECT * FROM tags t WHERE t.id = ? AND t.deletedAt IS NULL", id)
                .adjustedOptional(Tag.class).apply(this::addUser).get());
    }

    @Override
    public void deleteById(Long id) {
        executor.write(e -> e.execute("UPDATE tags t SET deleted_at = NOW() WHERE t.id = ?", id));
    }

    @Override
    public Tag save(Tag model) {
        executor.write(e ->  e.execute(
                "INSERT INTO tags (name, color, owner, deleted_at) VALUES (?, ?, ?, ?)",
                model.getName(), model.getColorHex(), model.getOwner().getId(), null
        ).affectOne().singleKey(Long.class, model::setId));
        return model;
    }

    @Override
    public List<Tag> findAllIncludeDeleted() {
        return executor.read(e -> e.executeQuery("SELECT * FROM tags").adjustedList(Tag.class).apply(this::addUser).get());
    }

    @Override
    public void restoreById(Long id) {
        executor.write(e -> e.execute("UPDATE tags t SET deleted_at = NULL WHERE t.id = ?", id));
    }

    @Override
    public Optional<Tag> findByIdIncludeDeleted(Long id) {
        return executor.read(e -> e.executeQuery("SELECT * FROM tags t WHERE t.id = ?")
                .adjustedOptional(Tag.class)
                .apply(this::addUser)
                .get());
    }

    @Override
    public List<Tag> getTagsByOwner(Long ownerId) {
        return executor.read(e -> e.executeQuery(
                "SELECT t.* FROM tags t " +
                    "INNER JOIN users u ON t.owner = u.id " +
                    "WHERE t.owner = ? " +
                    "AND t.deleted_at IS NULL AND u.deleted_at IS NULL", ownerId
        ).adjustedList(Tag.class).apply(this::addUser).get());
    }

    @Override
    public List<Tag> getTagsByOwnerAndName(Long ownerId, String name) {
        return executor.read (e -> e.executeQuery(
                "SELECT t.* FROM tags t " +
                    "INNER JOIN users u ON t.owner = u.id " +
                    "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', ?, '%')) " +
                    "AND u.id = ? " +
                    "AND u.deletedAt IS NULL " +
                    "AND t.deletedAt IS NULL",
        name, ownerId).adjustedList(Tag.class).apply(this::addUser).get());
    }
}

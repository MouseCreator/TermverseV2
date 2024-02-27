package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.model.id.LongIdIterable;
import org.hibernate.annotations.*;

@Data
@Entity
@Table(name = "users")
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@NoArgsConstructor
public class User implements LongIdIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;
    @Nonnull
    private Boolean deleted = false;
    private String profilePictureUrl;
}

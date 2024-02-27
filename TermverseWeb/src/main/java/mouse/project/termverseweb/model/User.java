package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.model.id.LongIdIterable;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements LongIdIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;
    private String profilePictureUrl;
}

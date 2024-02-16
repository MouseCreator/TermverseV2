package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    private String name;
    private String profilePictureUrl;
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    private String name;
    private String email;
    private String profilePictureUrl;
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;
    private String profilePictureUrl;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "users_study_sets",
            inverseJoinColumns = @JoinColumn(name = "study_set_id"),
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserStudySet> studySets = new ArrayList<>();
}

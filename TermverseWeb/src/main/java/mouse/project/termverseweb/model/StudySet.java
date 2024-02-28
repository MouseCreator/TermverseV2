package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "study_sets")
public class StudySet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;
    @Nonnull
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "users_study_sets",
            joinColumns = @JoinColumn(name = "study_set_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserStudySet> users = new ArrayList<>();
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
@Table(name = "users_study_sets")
public class UserStudySet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "study_set_id")
    private StudySet studySet;
    @Nonnull
    private String type;
    public UserStudySet(Long id, User user, StudySet studySet, String type) {
        this.id = id;
        this.user = user;
        this.studySet = studySet;
        this.type = type;
    }
}

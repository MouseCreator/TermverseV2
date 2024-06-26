package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users_study_sets")
@IdClass(UserStudySetId.class)
@NoArgsConstructor
public class UserStudySet {

    @ManyToOne
    @Nonnull
    @Id
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Nonnull
    @Id
    @JoinColumn(name = "study_set_id")
    private StudySet studySet;
    @Nonnull
    private String type;
    public UserStudySet(@Nonnull User user, @Nonnull StudySet set, @Nonnull String type) {
        this.user = user;
        this.studySet = set;
        this.type = type;
    }
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.lib.service.model.LongIterable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users_study_sets")
public class UserStudySet implements LongIterable {
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
}

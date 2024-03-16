package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.util.ArrayList;
import java.util.List;

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

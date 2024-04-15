package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "study_sets_terms")
@IdClass(SetTermId.class)
public class SetTerm {
    @ManyToOne
    @Nonnull
    @Id
    private StudySet set;
    @ManyToOne
    @Nonnull
    @Id
    private Term term;

    public SetTerm(@Nonnull StudySet set, @Nonnull Term term) {
        this.set = set;
        this.term = term;
    }
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.lib.service.model.LongIterable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users_terms")
@IdClass(UserTermId.class)
public class UserTerm {

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "user_id")
    @Id
    private User user;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "term_id")
    @Id
    private Term term;
    @Nonnull
    private String progress;

    public UserTerm(@Nonnull User user,@Nonnull Term term,@Nonnull String progress) {
        this.user = user;
        this.term = term;
        this.progress = progress;
    }
}

package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users_terms")
public class UserTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "term_id")
    private Term term;
    @Nonnull
    private String progress;
}

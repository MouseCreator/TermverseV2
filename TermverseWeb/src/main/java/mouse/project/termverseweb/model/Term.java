package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "terms")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String term;
    @Nonnull
    private String definition;
    private String hint;
    private String picture_url;
    @Nonnull
    private Integer order;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Nonnull
    private Long setId;

}

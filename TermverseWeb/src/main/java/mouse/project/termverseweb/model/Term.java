package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "terms")
public class Term implements LongIterable {
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
    @Column(name = "term_order")
    private Integer order;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Term(@Nonnull Long id) {
        this.id = id;
    }
}

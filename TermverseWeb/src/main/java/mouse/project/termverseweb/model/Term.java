package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "terms")
@Model
public class Term implements LongIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    @NamedColumn
    private Long id;
    @Nonnull
    @NamedColumn
    private String term;
    @Nonnull
    @NamedColumn
    private String definition;
    @NamedColumn
    private String hint;
    @NamedColumn("picture_url")
    private String picture_url;
    @Nonnull
    @Column(name = "term_order")
    @NamedColumn
    private Integer order;
    @Column(name = "deleted_at")
    @NamedColumn
    private LocalDateTime deletedAt;

    public Term(@Nonnull Long id) {
        this.id = id;
    }
}

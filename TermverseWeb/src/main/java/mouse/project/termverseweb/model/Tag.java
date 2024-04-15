package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tags")
@NoArgsConstructor
@Model
public class Tag implements LongIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    @NamedColumn
    private Long id;
    @Nonnull
    @NamedColumn
    private String name;
    @Nonnull
    @Column(name = "color")
    @NamedColumn
    private String colorHex;
    @Nonnull
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @Column(name = "deleted_at")
    @NamedColumn
    private LocalDateTime deletedAt;

    public Tag(@Nonnull Long id) {
        this.id = id;
    }
}

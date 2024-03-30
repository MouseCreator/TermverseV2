package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tags")
@NoArgsConstructor
public class Tag implements LongIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    private Long id;
    @Nonnull
    private String name;
    @Nonnull
    @Column(name = "color")
    private String colorHex;
    @Nonnull
    @ManyToOne
    private User owner;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

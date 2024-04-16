package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.termverseweb.lib.service.model.LongIterable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "study_sets")
@Model
public class StudySet implements LongIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    @NamedColumn
    private Long id;
    @Nonnull
    @NamedColumn
    private String name;
    @Column(name = "picture_url")
    @NamedColumn
    private String pictureUrl = null;
    @Nonnull
    @NamedColumn
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NamedColumn
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;
    @ManyToMany(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "users_study_sets",
            joinColumns = @JoinColumn(name = "study_set_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "study_sets_terms",
            joinColumns = @JoinColumn(name = "set_id"),
            inverseJoinColumns = @JoinColumn(name = "term_id")
    )
    @OrderBy("order ASC")
    private List<Term> terms = new ArrayList<>();
    public StudySet(@Nonnull Long id) {
        this.id = id;
    }
}

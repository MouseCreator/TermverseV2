package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.persistence.Table;
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

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@Model
public class User implements LongIterable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nonnull
    @NamedColumn
    private Long id;
    @Nonnull
    @NamedColumn
    private String name;
    @NamedColumn
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;
    @NamedColumn("profile_picture_url")
    private String profilePictureUrl;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_study_sets",
            inverseJoinColumns = @JoinColumn(name = "study_set_id"),
            joinColumns = @JoinColumn(name = "user_id")
    )
    private List<StudySet> studySets = new ArrayList<>();
    public User(@Nonnull Long id) {
        this.id = id;
    }
}

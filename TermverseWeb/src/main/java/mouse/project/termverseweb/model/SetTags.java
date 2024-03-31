package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "set_tags")
@IdClass(SetTagId.class)
public class SetTags {
    @ManyToOne
    @Nonnull
    @Id
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Nonnull
    @Id
    @JoinColumn(name = "set_id")
    private StudySet studySet;

    @ManyToOne
    @Nonnull
    @Id
    @JoinColumn(name = "tag_id")
    private Tag tag;
}

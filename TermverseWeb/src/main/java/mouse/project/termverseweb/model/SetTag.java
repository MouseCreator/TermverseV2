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
public class SetTag {
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

    public SetTag(@Nonnull User user,@Nonnull StudySet studySet,@Nonnull Tag tag) {
        this.user = user;
        this.studySet = studySet;
        this.tag = tag;
    }
}

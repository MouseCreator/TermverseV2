package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Model
public class UserStudySetModelFull {
    @NamedColumn("users.id")
    private Long userId;
    @NamedColumn("users.name")
    private String userName;
    @NamedColumn("users.profile_picture_url")
    private String userPictureUrl;
    @NamedColumn("users_study_sets.type")
    private String type;
    @NamedColumn("study_sets.id")
    private Long setId;
    @NamedColumn("study_sets.name")
    private String setName;
    @NamedColumn("study_sets.created_at")
    private LocalDateTime setCreatedAt;
    @NamedColumn("study_sets.picture_url")
    private String setPictureUrl;
}

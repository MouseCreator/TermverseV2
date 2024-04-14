package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Data
@NoArgsConstructor
public class UserStudySetModel {
    @NamedColumn("user_id")
    private Long userId;
    @NamedColumn("set_id")
    private Long setId;
    @NamedColumn("type")
    private String type;
}

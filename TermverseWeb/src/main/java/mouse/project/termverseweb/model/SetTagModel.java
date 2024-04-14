package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Data
@NoArgsConstructor
@Model
public class SetTagModel {
    @NamedColumn("user_id")
    private Long userId;
    @NamedColumn("set_id")
    private Long setId;
    @NamedColumn("tag_id")
    private Long tagId;
}

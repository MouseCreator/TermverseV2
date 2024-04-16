package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Data
@NoArgsConstructor
@Model
public class UserTermModel {
    @NamedColumn("user_id")
    private Long userId;
    @NamedColumn("term_id")
    private Long termId;
    @NamedColumn("progress")
    private String progress;
}

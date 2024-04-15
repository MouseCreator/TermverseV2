package mouse.project.termverseweb.model;

import lombok.Data;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Data
@Model
public class SetTermModel {
    @NamedColumn("set_id")
    private Long setId;
    @NamedColumn("term_id")
    private Long termId;
}

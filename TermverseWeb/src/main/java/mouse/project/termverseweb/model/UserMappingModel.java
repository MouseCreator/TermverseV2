package mouse.project.termverseweb.model;

import lombok.Data;
import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;

@Model
@Data
public class UserMappingModel {
    @NamedColumn("security_id")
    private String securityId;
    @NamedColumn("user_id")
    private Long userId;
}

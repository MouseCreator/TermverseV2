package mouse.project.termverseweb.dto.settag;

import lombok.Data;

@Data
public class SetTagUpdateDTO {
    private Long userId;
    private Long studySetId;
    private Long tagId;
}

package mouse.project.termverseweb.dto.userstudyset;

import lombok.Data;

@Data
public class UserStudySetResponseDTO {
    private Long id;
    private Long userId;
    private Long studySetId;
    private String type;
}

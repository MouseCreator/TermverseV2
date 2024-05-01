package mouse.project.termverseweb.dto.userstudyset;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStudySetCreateDTO {
    private Long userId;
    private Long studySetId;
    private String type;

    public UserStudySetCreateDTO(Long userId, Long studySetId, String type) {
        this.userId = userId;
        this.studySetId = studySetId;
        this.type = type;
    }
}

package mouse.project.termverseweb.dto.studyset;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class StudySetDescriptionWithProgressDTO {
    private Long id;
    private String name;
    private String pictureUrl;
    private LocalDateTime createdAt;
    private Integer size;
    private Integer termsLearned;
}

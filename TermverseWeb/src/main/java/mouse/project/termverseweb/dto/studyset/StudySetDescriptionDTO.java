package mouse.project.termverseweb.dto.studyset;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class StudySetDescriptionDTO {
    private Long id;
    private String name;
    private String pictureUrl;
    private LocalDateTime createdAt;
    private Integer size;
}

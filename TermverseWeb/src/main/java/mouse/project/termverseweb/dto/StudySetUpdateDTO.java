package mouse.project.termverseweb.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class StudySetUpdateDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}

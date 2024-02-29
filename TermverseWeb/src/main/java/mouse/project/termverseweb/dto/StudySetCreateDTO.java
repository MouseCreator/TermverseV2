package mouse.project.termverseweb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudySetCreateDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}

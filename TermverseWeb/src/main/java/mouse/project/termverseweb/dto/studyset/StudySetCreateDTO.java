package mouse.project.termverseweb.dto.studyset;

import lombok.Data;
import mouse.project.termverseweb.dto.genetic.CreateDTO;
import mouse.project.termverseweb.model.StudySet;

import java.time.LocalDateTime;

@Data
public class StudySetCreateDTO implements CreateDTO<StudySet> {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}

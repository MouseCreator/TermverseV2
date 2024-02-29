package mouse.project.termverseweb.dto.studyset;

import lombok.Data;
import mouse.project.termverseweb.dto.genetic.UpdateDTO;
import mouse.project.termverseweb.model.StudySet;

import java.time.LocalDateTime;
@Data
public class StudySetUpdateDTO implements UpdateDTO<StudySet> {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}

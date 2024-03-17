package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserStudySetId implements Serializable {
    @Serial
    private static final long serialVersionUID = -4316510197130191443L;
    private User user;
    private StudySet studySet;

}

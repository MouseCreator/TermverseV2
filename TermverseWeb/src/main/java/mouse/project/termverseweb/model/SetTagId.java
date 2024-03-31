package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
@Data
@NoArgsConstructor
public class SetTagId implements Serializable {

    @Serial
    private static final long serialVersionUID = -4109462841322466051L;
    private User user;
    private StudySet studySet;
    private Tag tag;
}

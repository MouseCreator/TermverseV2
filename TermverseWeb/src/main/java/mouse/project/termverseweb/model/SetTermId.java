package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SetTermId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4663100887813663341L;
    private StudySet set;
    private Term term;
}

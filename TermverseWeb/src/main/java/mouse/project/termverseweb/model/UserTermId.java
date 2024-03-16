package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserTermId implements Serializable {
    private User user;
    private Term term;
    @Serial
    private static final long serialVersionUID = 1L;
}

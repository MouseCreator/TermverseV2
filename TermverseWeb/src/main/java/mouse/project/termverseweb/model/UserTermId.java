package mouse.project.termverseweb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserTermId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4369982846685769906L;
    private User user;
    private Term term;

}

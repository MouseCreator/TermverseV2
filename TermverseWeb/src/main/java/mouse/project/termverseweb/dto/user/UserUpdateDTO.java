package mouse.project.termverseweb.dto.user;

import lombok.Data;
import mouse.project.termverseweb.dto.genetic.UpdateDTO;
import mouse.project.termverseweb.model.User;

@Data
public class UserUpdateDTO implements UpdateDTO<User> {
    private Long id;
    private String name;
    private String profilePictureUrl;
}

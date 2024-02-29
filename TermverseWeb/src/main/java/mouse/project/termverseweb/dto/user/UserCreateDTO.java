package mouse.project.termverseweb.dto.user;

import lombok.Data;
import mouse.project.termverseweb.dto.genetic.CreateDTO;
import mouse.project.termverseweb.model.User;

@Data
public class UserCreateDTO implements CreateDTO<User> {
    private String name;
    private String profilePictureUrl;
}

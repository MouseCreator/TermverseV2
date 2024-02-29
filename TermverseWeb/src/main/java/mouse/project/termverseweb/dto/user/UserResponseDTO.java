package mouse.project.termverseweb.dto.user;


import lombok.Data;
import mouse.project.termverseweb.dto.genetic.ResponseDTO;
import mouse.project.termverseweb.model.User;

@Data
public class UserResponseDTO implements ResponseDTO<User> {
    private Long id;
    private String name;
    private String profilePictureUrl;
}

package mouse.project.termverseweb.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private Long id;
    private String name;
    private String profilePictureUrl;
}

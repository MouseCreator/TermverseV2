package mouse.project.termverseweb.dto.user;

import lombok.Data;

@Data
public class UserWithRelation {
    private UserResponseDTO responseDTO;
    private String relationType;
}

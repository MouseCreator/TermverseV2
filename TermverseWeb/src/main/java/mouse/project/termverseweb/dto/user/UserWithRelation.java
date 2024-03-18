package mouse.project.termverseweb.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWithRelation {
    private UserResponseDTO responseDTO;
    private String relationType;

    public UserWithRelation(UserResponseDTO response, String type) {
        this.responseDTO = response;
        this.relationType = type;
    }
}

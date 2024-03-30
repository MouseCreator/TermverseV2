package mouse.project.termverseweb.dto.tag;

import lombok.Data;
@Data
public class TagResponseDTO {
    private Long id;
    private String name;
    private String colorHex;
    private Long ownerId;
}

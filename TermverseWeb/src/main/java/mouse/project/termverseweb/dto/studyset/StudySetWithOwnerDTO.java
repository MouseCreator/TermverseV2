package mouse.project.termverseweb.dto.studyset;

import lombok.Data;
import mouse.project.termverseweb.dto.user.UserResponseDTO;

import java.time.LocalDateTime;
@Data
public class StudySetWithOwnerDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String pictureUrl;
    private UserResponseDTO owner;
    public StudySetWithOwnerDTO(StudySetResponseDTO responseDTO, UserResponseDTO owner) {
        id = responseDTO.getId();
        name = responseDTO.getName();
        createdAt = responseDTO.getCreatedAt();
        pictureUrl = responseDTO.getPictureUrl();
        this.owner = owner;
    }

    public StudySetWithOwnerDTO(StudySetDescriptionDTO responseDTO, UserResponseDTO owner) {
        id = responseDTO.getId();
        name = responseDTO.getName();
        createdAt = responseDTO.getCreatedAt();
        pictureUrl = responseDTO.getPictureUrl();
        this.owner = owner;
    }
}

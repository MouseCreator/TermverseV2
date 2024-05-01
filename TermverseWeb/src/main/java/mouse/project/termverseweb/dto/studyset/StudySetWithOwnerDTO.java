package mouse.project.termverseweb.dto.studyset;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class StudySetWithOwnerDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String pictureUrl;
    private String owner;
    public StudySetWithOwnerDTO(StudySetResponseDTO responseDTO, String owner) {
        id = responseDTO.getId();
        name = responseDTO.getName();
        createdAt = responseDTO.getCreatedAt();
        pictureUrl = responseDTO.getPictureUrl();
        this.owner = owner;
    }

    public StudySetWithOwnerDTO(StudySetDescriptionDTO responseDTO, String owner) {
        id = responseDTO.getId();
        name = responseDTO.getName();
        createdAt = responseDTO.getCreatedAt();
        pictureUrl = responseDTO.getPictureUrl();
        this.owner = owner;
    }
}

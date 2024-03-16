package mouse.project.termverseweb.dto.progress;

import lombok.Data;

@Data
public class TermProgressResponseDTO {
    private Long userId;
    private Long termId;
    private String progress;
}

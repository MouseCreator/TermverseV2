package mouse.project.termverseweb.dto.term;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TermResponseDTO {
    private Long id;
    private String term;
    private String definition;
    private String hint;
    private String picture_url;
    private Integer order;
    private LocalDateTime deletedAt;
}

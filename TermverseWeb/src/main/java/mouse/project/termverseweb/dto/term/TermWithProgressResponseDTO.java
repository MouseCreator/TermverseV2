package mouse.project.termverseweb.dto.term;

import lombok.Data;

@Data
public class TermWithProgressResponseDTO {
    private Long id;
    private String term;
    private String definition;
    private String hint;
    private String picture_url;
    private Integer order;
    private String progress;
}

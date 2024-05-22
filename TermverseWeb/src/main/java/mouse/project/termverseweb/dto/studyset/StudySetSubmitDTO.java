package mouse.project.termverseweb.dto.studyset;

import lombok.Data;
import mouse.project.termverseweb.dto.term.TermSubmitDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudySetSubmitDTO {
    private Long id;
    private String name;
    private String pictureUrl;
    private List<TermSubmitDTO> terms = new ArrayList<>();
}

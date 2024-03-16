package mouse.project.termverseweb.dto.progress;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TermProgressPair {

    private Long term;
    private String progress;

    public TermProgressPair(Long term, String progress) {
        this.term = term;
        this.progress = progress;
    }
}

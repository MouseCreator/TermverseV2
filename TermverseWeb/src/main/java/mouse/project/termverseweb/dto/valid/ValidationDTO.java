package mouse.project.termverseweb.dto.valid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidationDTO {
    private String status;

    public ValidationDTO(String status) {
        this.status = status;
    }
}

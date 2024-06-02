package mouse.project.termverseweb.dto.pages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TotalPagesDTO {
    int totalSets;
    int totalPages;

    public TotalPagesDTO(int totalSets, int totalPages) {
        this.totalSets = totalSets;
        this.totalPages = totalPages;
    }
}

package mouse.project.termverseweb.service.search;

import mouse.project.lib.data.page.PageDescription;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;

import java.util.List;

public interface SearchCategoryHandler {
    List<StudySetWithOwnerDTO> search(String query, Long userId, String sort, PageDescription page);
    String forCategory();
}

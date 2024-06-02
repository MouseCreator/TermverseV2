package mouse.project.termverseweb.service.search;

import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageDescriptionImpl;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.termverseweb.defines.Categories;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.pages.TotalPagesDTO;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;
import mouse.project.termverseweb.mapper.StudySetWithOwnerMapper;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Service
public class AllSearchCategory implements SearchCategoryHandler {
    private final StudySetRepository studySetRepository;
    private final StudySetWithOwnerMapper studySetWithOwnerMapper;
    @Auto
    @Autowired
    public AllSearchCategory(StudySetRepository studySetRepository, StudySetWithOwnerMapper studySetWithOwnerMapper) {
        this.studySetRepository = studySetRepository;
        this.studySetWithOwnerMapper = studySetWithOwnerMapper;
    }

    @Override
    @Transactional
    public List<StudySetWithOwnerDTO> search(String query, Long userId, String sort, PageDescription page) {
        Page<UserStudySet> allByNameAndUser = studySetRepository.findAllByNameAndType(query, UserStudySetRelation.OWNER, page, sort);
        return allByNameAndUser.getElements()
                .stream()
                .map(u -> studySetWithOwnerMapper.toStudySetWithOwner(u.getUser(), u.getStudySet()))
                .toList();
    }

    @Override
    public TotalPagesDTO totalPages(String query, Long usedId, String sort, PageDescription pageDescription) {
        PageDescription page = new PageDescriptionImpl(0, Integer.MAX_VALUE);
        Page<UserStudySet> allByNameAndUser = studySetRepository.findAllByNameAndType(query, UserStudySetRelation.OWNER, page, sort);
        int count = allByNameAndUser.getElements().size();
        int pages;
        if (count == 0) {
            pages = 0;
        } else {
            pages = (count - 1) / pageDescription.size() + 1;
        }
        return new TotalPagesDTO(count, pages);
    }

    @Override
    public String forCategory() {
        return Categories.ALL;
    }
}

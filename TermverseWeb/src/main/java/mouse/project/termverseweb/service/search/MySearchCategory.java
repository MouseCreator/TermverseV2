package mouse.project.termverseweb.service.search;

import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.termverseweb.defines.Categories;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;
import mouse.project.termverseweb.mapper.StudySetWithOwnerMapper;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.repository.StudySetRepository;

import java.util.List;

public class MySearchCategory implements SearchCategoryHandler {
    private final StudySetRepository studySetRepository;
    private final StudySetWithOwnerMapper studySetWithOwnerMapper;

    public MySearchCategory(StudySetRepository studySetRepository, StudySetWithOwnerMapper studySetWithOwnerMapper) {
        this.studySetRepository = studySetRepository;
        this.studySetWithOwnerMapper = studySetWithOwnerMapper;
    }

    @Override
    @Transactional
    public List<StudySetWithOwnerDTO> search(String query, Long userId, PageDescription page) {
        Page<UserStudySet> allByNameAndUser = studySetRepository.findAllByNameAndUser(query,
                userId, UserStudySetRelation.OWNER, page);
        return allByNameAndUser.getElements()
                .stream()
                .filter(u -> u.getUser().getId().equals(userId))
                .map(u -> studySetWithOwnerMapper.toStudySetWithOwner(u.getUser(), u.getStudySet()))
                .toList();
    }

    @Override
    public String forCategory() {
        return Categories.MY_SETS;
    }
}

package mouse.project.termverseweb.service.search;

import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.defines.Categories;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.studyset.StudySetWithOwnerDTO;
import mouse.project.termverseweb.mapper.StudySetWithOwnerMapper;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.service.sort.StudySetSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AllSearchCategory implements SearchCategoryHandler {
    private final StudySetRepository studySetRepository;
    private final StudySetWithOwnerMapper studySetWithOwnerMapper;
    private final StudySetSorter sorter;
    @Auto
    @Autowired
    public AllSearchCategory(StudySetRepository studySetRepository, StudySetWithOwnerMapper studySetWithOwnerMapper, StudySetSorter sort) {
        this.studySetRepository = studySetRepository;
        this.studySetWithOwnerMapper = studySetWithOwnerMapper;
        this.sorter = sort;
    }

    @Override
    @Transactional
    public List<StudySetWithOwnerDTO> search(String query, Long userId, String sort, PageDescription page) {
        Page<UserStudySet> allByNameAndUser = studySetRepository.findAllByNameAndType(query, UserStudySetRelation.OWNER, page);
        return allByNameAndUser.getElements()
                .stream()
                .sorted(sorter.chooseComparator(sort))
                .map(u -> studySetWithOwnerMapper.toStudySetWithOwner(u.getUser(), u.getStudySet()))
                .toList();
    }

    @Override
    public String forCategory() {
        return Categories.ALL;
    }
}

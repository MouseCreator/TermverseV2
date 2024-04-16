package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermUpdateDTO;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.TermFactory;
import mouse.project.termverseweb.repository.StudySetTermRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TermServiceImplTest {
    private final TermService service;
    private final StudySetService studySetService;
    private final StudySetTermRepository setTermRepository;
    private final Factories factories;
    private final SoftDeletionTest soft;
    @Autowired
    public TermServiceImplTest(TermService termService,
                               StudySetService studySetService, StudySetTermRepository setTermRepository,
                               Factories factories,
                               SoftDeletionTest soft) {
        this.service = termService;
        this.studySetService = studySetService;
        this.setTermRepository = setTermRepository;
        this.factories = factories;
        this.soft = soft;
    }

    private SoftDeletionTest.BeforeSoftDeletion<TermResponseDTO, Long> soft() {
        return soft.using(service::removeById, TermResponseDTO::getId);
    }

    private List<TermCreateDTO> createTerms(String base, int count) {
        TermFactory termFactory = factories.getFactory(TermFactory.class);
        List<TermCreateDTO> list = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            TermCreateDTO termCreateDTO = termFactory.termCreateDTO(base + i, i);
            list.add(termCreateDTO);
        }
        return list;
    }

    private TermCreateDTO createTerm(String term, String definition) {
        TermFactory termFactory = factories.getFactory(TermFactory.class);
        TermCreateDTO termCreateDTO = termFactory.termCreateDTO(term, 1);
        termCreateDTO.setDefinition(definition);
        return termCreateDTO;
    }

    private List<TermResponseDTO> createAndSave(String base, int count) {
        List<TermCreateDTO> terms = createTerms(base, count);
        return saveTerms(terms);
    }

    private List<TermResponseDTO> saveTerms(List<TermCreateDTO> terms) {
        return terms.stream().map(service::save).toList();
    }
    private StudySetResponseDTO createStudySetAndBind(String setName, List<TermResponseDTO> terms) {
        StudySetCreateDTO studySetCreateDTO = factories.getFactory(StudySetFactory.class).studySetCreateDTO(setName);
        StudySetResponseDTO saved = studySetService.save(studySetCreateDTO);
        Long setId = saved.getId();
        terms.forEach(t -> {
            Term term = new Term(t.getId());
            StudySet studySet = new StudySet(setId);
            SetTerm setTerm = new SetTerm(studySet, term);
            setTermRepository.save(setTerm);
        });
        return saved;
    }
    @Test
    void save() {
        String term = "term";
        String definition = "definition";
        TermCreateDTO cTerm = createTerm(term, definition);
        TermResponseDTO saved = service.save(cTerm);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(term, saved.getTerm());
        assertEquals(definition, saved.getDefinition());
    }
    @Test
    void getAll() {
        List<TermResponseDTO> inserted = createAndSave("get-all", 3);

        soft().passAll(inserted)
                        .validatePresentIn(service::getAll);
        soft().removeAll(inserted)
                .validateAbsentIn(service::getAll);
    }

    @Test
    void getById() {
        List<TermResponseDTO> inserted = createAndSave("get-all", 1);
        TermResponseDTO expected = inserted.get(0);
        TermResponseDTO actual = service.getById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        List<TermResponseDTO> inserted = createAndSave("get-all", 1);
        TermResponseDTO only = inserted.get(0);
        Long id = only.getId();
        TermUpdateDTO updateDTO = factories.getFactory(TermFactory.class).termUpdateDTO(only, "new-term");
        TermResponseDTO update = service.update(updateDTO);
        TermResponseDTO byId = service.getById(id);
        assertEquals(update, byId);
        assertNotEquals(only, byId);

        TermUpdateDTO notExisting = new TermUpdateDTO();
        notExisting.setId(Long.MAX_VALUE);
        assertThrows(EntityNotFoundException.class, () -> service.update(notExisting));
    }

    @Test
    void removeById() {
        TermResponseDTO toRemove = createAndSave("to-remove", 1).get(0);
        service.removeById(toRemove.getId());
        assertThrows(EntityNotFoundException.class, () -> service.getById(toRemove.getId()));
    }

    @Test
    void getAllWithDeleted() {
        TermResponseDTO toRemove = createAndSave("to-remove", 1).get(0);
        service.removeById(toRemove.getId());
        assertTrue(service.getAllWithDeleted().contains(toRemove));
    }

    @Test
    void restoreById() {
        TermResponseDTO toRemove = createAndSave("to-remove", 1).get(0);
        service.removeById(toRemove.getId());
        assertThrows(EntityNotFoundException.class, () -> service.getById(toRemove.getId()));
        service.restoreById(toRemove.getId());
        assertEquals(toRemove, service.getById(toRemove.getId()));
    }

    @Test
    void getByStudySet() {
        List<TermResponseDTO> addedTerms = createAndSave("w-set", 3);
        StudySetResponseDTO holder = createStudySetAndBind("holder", addedTerms);
        Long setId = holder.getId();
        List<TermResponseDTO> byStudySet = service.getByStudySet(setId);
        assertEquals(addedTerms, byStudySet);

        List<TermResponseDTO> subs = addedTerms.subList(0, 1);
        soft().removeAll(subs).validateAbsentIn(() -> service.getByStudySet(setId));
    }
}
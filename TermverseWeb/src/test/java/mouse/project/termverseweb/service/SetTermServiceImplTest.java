package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.TermFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SetTermServiceImplTest {

    private final SetTermService service;
    private final TermService termService;
    private final StudySetService studySetService;
    private final Factories factories;
    @Autowired
    public SetTermServiceImplTest(SetTermService service,
                                  TermService termService,
                                  StudySetService studySetService,
                                  Factories factories) {
        this.service = service;
        this.termService = termService;
        this.studySetService = studySetService;
        this.factories = factories;
    }
    private StudySetResponseDTO createAndSaveSet(String setName) {
        StudySetFactory studySetFactory = factories.getFactory(StudySetFactory.class);
        StudySetCreateDTO createDTO = studySetFactory.studySetCreateDTO(setName);
        return studySetService.save(createDTO);
    }
    private TermResponseDTO createAndSaveTerm(String termName, int order) {
        TermFactory termFactory = factories.getFactory(TermFactory.class);
        TermCreateDTO termCreateDTO = termFactory.termCreateDTO(termName, order);
        return termService.save(termCreateDTO);
    }

    private List<SetTerm> saveInstances(StudySetResponseDTO studySet, List<TermResponseDTO> terms) {
        return terms.stream().map(t -> service.save(studySet.getId(), t.getId())).toList();
    }

    private List<SetTerm> createAndSaveInstances(String base, int termCount) {
        StudySetResponseDTO set = createAndSaveSet(base + "-set");
        List<TermResponseDTO> terms = new ArrayList<>();
        for (int i = 1; i <= termCount; i++) {
            TermResponseDTO term = createAndSaveTerm(base + "-term" + i, i);
            terms.add(term);
        }
        return saveInstances(set, terms);
    }
    private SetTerm createAndSaveInstance(String base) {
        return createAndSaveInstances(base, 1).get(0);
    }
    @Test
    void save() {
        int size = 2;
        List<SetTerm> saved = createAndSaveInstances("saved", size);
        assertEquals(size, saved.size());
    }

    @Test
    void saveInvalidData() {
        TermResponseDTO term = createAndSaveTerm("term1", 1);
        StudySetResponseDTO set = createAndSaveSet("set1");
        assertThrows(EntityNotFoundException.class, () -> service.save(set.getId(), Long.MAX_VALUE));
        assertThrows(EntityNotFoundException.class, () -> service.save(Long.MAX_VALUE, term.getId()));
    }
    @Test
    void getAll() {
        List<SetTerm> instances = createAndSaveInstances("get-all", 4);
        List<SetTerm> all = service.getAll();
        MTest.containsAll(all, instances);
        MTest.noDuplicates(all);
    }

    @Test
    void get() {
        SetTerm instance = createAndSaveInstance("get-id");
        Long setId = instance.getSet().getId();
        Long termId = instance.getTerm().getId();
        Optional<SetTerm> setTerm = service.get(setId, termId);
        assertTrue(setTerm.isPresent());
        assertEquals(instance, setTerm.get());

        assertTrue(service.get(setId, Long.MAX_VALUE).isEmpty());
        assertTrue(service.get(Long.MAX_VALUE, termId).isEmpty());
    }
    @Test
    void delete() {
        SetTerm instance = createAndSaveInstance("deleted");
        Long setId = instance.getSet().getId();
        Long termId = instance.getTerm().getId();
        service.delete(setId, termId);
        assertTrue(service.get(setId, termId).isEmpty());
    }
    @Test
    void getTermCount() {
        int count = 3;
        List<SetTerm> instances = createAndSaveInstances("count", count);
        Long setId = instances.get(0).getSet().getId();
        int termCount = service.getTermCount(setId);
        assertEquals(count, termCount);
    }


}
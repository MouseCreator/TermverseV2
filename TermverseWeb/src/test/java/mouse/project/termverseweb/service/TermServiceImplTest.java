package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.TermFactory;
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
    private final Factories factories;
    private final SoftDeletionTest soft;
    @Autowired
    public TermServiceImplTest(TermService termService,
                               StudySetService studySetService,
                              Factories factories,
                              SoftDeletionTest soft) {
        this.service = termService;
        this.studySetService = studySetService;
        this.factories = factories;
        this.soft = soft;
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

    private List<TermResponseDTO> saveTerms(List<TermCreateDTO> terms) {
        return terms.stream().map(service::save).toList();
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
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void removeById() {
    }

    @Test
    void getAllWithDeleted() {
    }

    @Test
    void restoreById() {
    }

    @Test
    void getByStudySet() {
    }
}
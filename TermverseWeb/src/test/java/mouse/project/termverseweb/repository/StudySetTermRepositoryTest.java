package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StudySetTermRepositoryTest {

    @InitBeforeEach
    private StudySetTermRepository repository;
    @InitBeforeEach
    private StudySetRepository studySetRepository;
    @InitBeforeEach
    private TermRepository termRepository;
    @InitBeforeEach
    private Insertions insertions;
    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }

    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }
    private List<SetTerm> insertData(String base, int count) {
        List<StudySet> sets = insertions.generateStudySets(base, 1);
        List<StudySet> savedSets = insertions.saveAll(studySetRepository, sets);
        StudySet studySet = savedSets.get(0);
        List<Term> terms = insertions.generateTerms(base, count);
        List<Term> savedTerms = insertions.saveAll(termRepository, terms);
        return insertions.bindSetTerms(repository, studySet, savedTerms);
    }

    @Test
    void save() {
        List<SetTerm> saved = insertData("saved", 4);
        assertEquals(4, saved.size());
    }

    @Test
    void findById() {
        List<SetTerm> search = insertData("search", 1);
        SetTerm setTerm = search.get(0);
        Long setId = setTerm.getSet().getId();
        Long termId = setTerm.getTerm().getId();

        Optional<SetTerm> byIdOptional = repository.findById(termId, setId);
        assertTrue(byIdOptional.isPresent());

        assertEquals(setTerm, byIdOptional.get());

        assertTrue(repository.findById(termId, Long.MAX_VALUE).isEmpty());
        assertTrue(repository.findById(Long.MAX_VALUE, setId).isEmpty());
    }

    @Test
    void findAll() {
        List<SetTerm> search = insertData("search-all", 1);
        List<SetTerm> all = repository.findAll();
        MTest.containsAll(all, search);
    }

    @Test
    void getTermsFromStudySet() {
        List<SetTerm> inputList = insertData("from-set", 3);
        Long setId = inputList.get(0).getSet().getId();
        List<Term> terms = inputList.stream().map(SetTerm::getTerm).toList();
        List<Term> termsFromStudySet = repository.getTermsFromStudySet(setId);
        MTest.compareUnordered(terms, termsFromStudySet);
    }

    @Test
    void getTermCount() {
    }

    @Test
    void delete() {
    }
}
package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
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

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TermRepositoryImplTest {

    @InitBeforeEach
    private StudySetRepository studySetRepository;
    @InitBeforeEach
    private TermRepository termRepository;
    @InitBeforeEach
    private Insertions insertions;
    @InitBeforeEach
    private SoftDeletionTest soft;

    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }
    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    private List<Term> insertData(String base, int count) {
        List<Term> terms = insertions.generateTerms(base, count);
        insertions.saveAll(termRepository, terms);
        return terms;
    }

    private SoftDeletionTest.BeforeSoftDeletion<Term, Long> soft() {
        return soft.using(termRepository::deleteById, Term::getId);
    }
    @Test
    void save() {
        List<Term> terms = insertData("saved", 2);
        assertEquals(3, terms.size());
    }
    @Test
    void findAll() {
        List<Term> terms = insertData("find-all", 3);
        List<Term> all = termRepository.findAll();
        MTest.containsAll(all, terms);

        termRepository.deleteById(terms.get(0).getId());

        List<Term> allAfter = termRepository.findAll();
        assertFalse(allAfter.contains(terms.get(0)));
    }

    @Test
    void findAllIncludeDeleted() {
        List<Term> terms = insertData("find-all", 2);
        soft().passAll(terms).byIds().validatePresentIn(() -> termRepository.findAllIncludeDeleted());
        soft().removeAll(terms).byIds().validatePresentIn(() -> termRepository.findAllIncludeDeleted());
    }

    @Test
    void findAllByStudySet() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByIdIncludeDeleted() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void restoreById() {
    }

    @Test
    void removeTermFormStudySetsById() {
    }

    @Test
    void findAllByIds() {
    }
}
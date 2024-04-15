package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
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
    }

    @Test
    void findAll() {
    }

    @Test
    void getTermsFromStudySet() {
    }

    @Test
    void getTermCount() {
    }

    @Test
    void delete() {
    }
}
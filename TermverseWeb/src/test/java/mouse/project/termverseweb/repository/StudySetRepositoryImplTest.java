package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.model.SetTerm;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.mouselib.TestContainer;
import mouse.project.termverseweb.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StudySetRepositoryImplTest {

    @InitBeforeEach
    private StudySetRepository repository;
    @InitBeforeEach
    private TermRepository termRepository;
    @InitBeforeEach
    private StudySetTermRepository setTermRepository;
    @InitBeforeEach
    private SoftDeletionTest soft;

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

    private List<StudySet> insertData(String base, int count) {
        List<StudySet> sets = insertions.generateStudySets(base, count);
        return insertions.saveAll(repository, sets);
    }
    private StudySet insertData(String base) {
        List<StudySet> sets = insertions.generateStudySets(base, 1);
        return insertions.saveAll(repository, sets).get(0);
    }
    @Test
    void save() {
        List<StudySet> sets = insertData("Save", 4);
        assertEquals(4, sets.size());
    }
    @Test
    void findAll() {
        List<StudySet> sets = insertData("Study sets All", 3);
        List<StudySet> all = repository.findAll();
        assertTrue(all.containsAll(sets));
    }

    @Test
    void findAllIncludeDeleted() {
        StudySet set = insertData("FAID");
        assertTrue(repository.findAllIncludeDeleted().contains(set));
        soft().remove(set).
                byIds().validatePresentIn(() -> repository.findAllIncludeDeleted());
    }

    @Test
    void findAllByNameIgnoreCase() {
        String specialName = "__SPECIAL_NAME__";
        String lower = specialName.toLowerCase();
        List<StudySet> sets = insertData(specialName, 2);

        List<StudySet> allByNameIgnoreCase = repository.findAllByNameIgnoreCase(lower);
        assertEquals(sets.size(), allByNameIgnoreCase.size());
        assertTrue(allByNameIgnoreCase.containsAll(sets));
    }

    @Test
    void findAllByCreatedDateRange() {
        List<StudySet> sets = insertions.generateStudySets("Time", 2);
        StudySet set1 = sets.get(0);
        set1.setCreatedAt(DateUtils.fromString("2010-01-10 10:00:00"));
        StudySet set2 = sets.get(1);
        set2.setCreatedAt(DateUtils.fromString("2010-01-12 10:00:00"));
        insertions.saveAll(repository, sets);

        LocalDateTime before = DateUtils.fromString("2010-01-08 10:00:00");
        LocalDateTime between = DateUtils.fromString("2010-01-11 10:00:00");
        LocalDateTime after = DateUtils.fromString("2010-01-12 10:00:00");
        List<StudySet> all = repository.findAllByCreatedDateRange(before, after);
        assertEquals(2, all.size());

        List<StudySet> first = repository.findAllByCreatedDateRange(before, between);
        assertEquals(1, first.size());
        assertEquals(set1, first.get(0));

        List<StudySet> second = repository.findAllByCreatedDateRange(between, after);
        assertEquals(1, second.size());
        assertEquals(set2, second.get(0));
    }

    @Test
    void deleteById() {
        StudySet studySet = insertData("Delete-by-id");
        Long id = studySet.getId();

        soft().remove(studySet)
                .byIds().assertTrue(()->repository.findById(id).isEmpty())
                .byIds().assertTrue(()->repository.findByIdIncludeDeleted(id).isPresent());
    }

    private SoftDeletionTest.BeforeSoftDeletion<StudySet, Long> soft() {
        return soft.using(repository::deleteById, StudySet::getId);
    }

    @Test
    void restoreById() {
        StudySet studySet = insertData("Restore-by-id");
        Long id = studySet.getId();

        soft().remove(studySet)
                .byIds().assertTrue(()->repository.findById(id).isEmpty())
                .restoreWith(repository::restoreById)
                .byIds().assertTrue(()->repository.findById(id).isPresent());
    }



    @Test
    void findById() {
        StudySet studySet = insertData("Find-by-id");
        Long id = studySet.getId();
        Optional<StudySet> byId = repository.findById(id);
        assertTrue(byId.isPresent());
        assertEquals(studySet, byId.get());

        Optional<StudySet> notExist = repository.findById(Long.MAX_VALUE);
        assertTrue(notExist.isEmpty());
    }

    @Test
    void findByIdIncludeDeleted() {
        StudySet studySet = insertData("Find-by-id-deleted");
        Long id = studySet.getId();
        repository.deleteById(id);
        Optional<StudySet> byId = repository.findByIdIncludeDeleted(id);
        assertTrue(byId.isPresent());
        Optional<StudySet> notExist = repository.findByIdIncludeDeleted(Long.MAX_VALUE);
        assertTrue(notExist.isEmpty());
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void testFindAllByUserId() {
    }

    @Test
    void findAllByIdWithTerms() {
        StudySet studySet = insertData("Find-by-id-deleted");
        List<Term> terms = insertTerms(studySet, "Terms1");
        Long id = studySet.getId();
        Optional<StudySet> allByIdWithTerms = repository.findAllByIdWithTerms(id);
        assertTrue(allByIdWithTerms.isPresent());
        StudySet studySetFromDB = allByIdWithTerms.get();
        assertNotNull(studySetFromDB.getTerms());
        MTest.compareUnordered(terms, studySetFromDB.getTerms());
    }

    private List<Term> insertTerms(StudySet studySet, String termsBaseName) {
        List<Term> terms = insertions.generateTerms(termsBaseName, 3);
        List<Term> savedTerms = insertions.saveAll(termRepository, terms);
        savedTerms.forEach(t -> setTermRepository.save(new SetTerm(studySet, t)));
        return savedTerms;
    }

    @Test
    void getTermCount() {
    }

    @Test
    void findByIdWithSize() {
    }
}
package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.model.StudySet;
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
class StudySetRepositoryImplTest {

    @InitBeforeEach
    private StudySetRepository repository;

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
    @Test
    void save() {
        List<StudySet> sets = insertData("Save", 4);
        assertEquals(4, sets.size());
    }
    @Test
    void findAll() {
        List<StudySet> sets = insertData("Study sets", 3);
        List<StudySet> all = repository.findAll();
        assertTrue(all.containsAll(sets));
    }

    @Test
    void findAllIncludeDeleted() {
    }

    @Test
    void findAllByNameIgnoreCase() {
    }

    @Test
    void findAllByCreatedDateRange() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void restoreById() {
    }



    @Test
    void findById() {
    }

    @Test
    void findByIdIncludeDeleted() {
    }

    @Test
    void findAllByUserId() {
    }

    @Test
    void testFindAllByUserId() {
    }

    @Test
    void findAllByIdWithTerms() {
    }

    @Test
    void getTermCount() {
    }

    @Test
    void findByIdWithSize() {
    }
}
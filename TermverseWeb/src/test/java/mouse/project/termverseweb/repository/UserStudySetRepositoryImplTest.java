package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserStudySet;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserStudySetRepositoryImplTest {

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }
    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private StudySetRepository studySetRepository;
    @InitBeforeEach
    private UserStudySetRepository repository;
    @InitBeforeEach
    private Insertions insertions;

    private List<UserStudySet> insertData(String base, int count) {
        List<User> users = insertions.generateUsers(base, 1);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user = savedUsers.get(0);

        List<StudySet> sets = insertions.generateStudySets(base, count);
        List<StudySet> savedSets = insertions.saveAll(studySetRepository, sets);
        List<String> relations = createRelations(count);
        return insertions.bindUserSets(repository, user, savedSets, relations);
    }
    @NotNull
    private static List<String> createRelations(int count) {
        List<String> relations = new ArrayList<>();
        if (count > 0) {
            relations.add(UserStudySetRelation.OWNER);
        }
        for (int i = 1; i < count; i++) {
            relations.add(UserStudySetRelation.VIEWER);
        }
        return relations;
    }

    @Test
    void save() {
        int size = 3;
        List<UserStudySet> userStudySets = insertData("saved", size);
        assertEquals(size, userStudySets.size());
    }
    @Test
    void findAll() {

    }

    @Test
    void findByUserAndStudySet() {
    }

    @Test
    void deleteByUserAndStudySet() {
    }

    @Test
    void findByUserAndType() {
    }

    @Test
    void findByUser() {
    }
}
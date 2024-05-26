package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
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
import java.util.Optional;

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
        User user = savedUsers.getFirst();

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

    private static void validateRelationTypes(int size, List<UserStudySet> userStudySets) {
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                assertEquals(UserStudySetRelation.OWNER, userStudySets.get(i).getType());
            } else {
                assertEquals(UserStudySetRelation.VIEWER, userStudySets.get(i).getType());
            }
        }
    }

    @Test
    void save() {
        int size = 3;
        List<UserStudySet> userStudySets = insertData("saved", size);
        assertEquals(size, userStudySets.size());

        validateRelationTypes(size, userStudySets);
    }



    @Test
    void findAll() {
        List<UserStudySet> userStudySets = insertData("find-all", 4);
        List<UserStudySet> all = repository.findAll();
        MTest.containsAll(all, userStudySets);
        MTest.noDuplicates(all);

    }

    @Test
    void findByUserAndStudySet() {
        List<UserStudySet> userStudySets = insertData("find-all", 4);
        UserStudySet target = userStudySets.get(0);
        User user = target.getUser();
        Long userId = user.getId();

        StudySet studySet = target.getStudySet();
        Long setId = studySet.getId();

        Optional<UserStudySet> actual = repository.findByUserAndStudySet(userId, setId);
        assertTrue(actual.isPresent());
        assertEquals(target, actual.get());
    }

    @Test
    void deleteByUserAndStudySet() {
        List<UserStudySet> userStudySets = insertData("delete", 2);
        UserStudySet target = userStudySets.get(0);
        User user = target.getUser();
        Long userId = user.getId();

        StudySet studySet = target.getStudySet();
        Long setId = studySet.getId();

        repository.deleteByUserAndStudySet(userId, setId);

        assertTrue(repository.findByUserAndStudySet(userId, setId).isEmpty());
        assertFalse(repository.findAll().contains(target));
    }

    @Test
    void findByUserAndType() {
        int size = 3;
        List<UserStudySet> userStudySets = insertData("type-search", size);
        UserStudySet owned = userStudySets.get(0);
        User user = owned.getUser();
        Long userId = user.getId();
        List<UserStudySet> ownedSets = repository.findByUserAndType(userId, UserStudySetRelation.OWNER);
        MTest.compareUnordered(List.of(owned), ownedSets);

        List<UserStudySet> viewedSets = repository.findByUserAndType(userId, UserStudySetRelation.VIEWER);
        MTest.compareUnordered(userStudySets.subList(1, size), viewedSets);
    }

    @Test
    void findByUser() {
        List<UserStudySet> userStudySets = insertData("delete", 2);
        UserStudySet target = userStudySets.get(0);
        User user = target.getUser();
        Long userId = user.getId();
        List<UserStudySet> byUser = repository.findByUser(userId);
        MTest.compareUnordered(userStudySets, byUser);
    }
}
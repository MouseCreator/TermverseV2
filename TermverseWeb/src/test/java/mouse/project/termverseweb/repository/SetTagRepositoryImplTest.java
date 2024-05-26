package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.model.SetTag;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.model.Tag;
import mouse.project.termverseweb.model.User;
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
class SetTagRepositoryImplTest {
    @InitBeforeEach
    private StudySetRepository studySetRepository;
    @InitBeforeEach
    private TagRepository tagRepository;
    @InitBeforeEach
    private SetTagRepository repository;
    @InitBeforeEach
    private UserRepository userRepository;
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

    private List<SetTag> insertData(String base, int count) {
        List<User> users = insertions.generateUsers(base, 1);
        User user = insertions.saveAll(userRepository, users).get(0);

        List<StudySet> studySets = insertions.generateStudySets(base, 1);
        StudySet studySet = insertions.saveAll(studySetRepository, studySets).get(0);

        List<Tag> tags = insertions.generateTags(user, base, count);
        List<Tag> savedTags = insertions.saveAll(tagRepository, tags);

        return insertions.bindTags(repository, user, studySet, savedTags);
    }

    @Test
    void save() {
        int size = 4;
        List<SetTag> saved = insertData("saved", size);
        assertEquals(size, saved.size());
    }
    @Test
    void getAll() {
        List<SetTag> setTags = insertData("get-all", 3);
        List<SetTag> all = repository.getAll();
        MTest.containsAll(all, setTags);
    }
    @Test
    void getSetTagById() {
        SetTag setTag = insertData("get-all", 1).getFirst();
        Long userId = setTag.getUser().getId();
        Long setId = setTag.getStudySet().getId();
        Long tagId = setTag.getTag().getId();

        Optional<SetTag> setTagById = repository.getSetTagById(userId, setId, tagId);
        assertTrue(setTagById.isPresent());
        assertEquals(setTag, setTagById.get());
    }

    @Test
    void getStudySetsByUserAndTags() {
        List<SetTag> setTags = insertData("get-all", 4);
        Long userId = setTags.get(0).getUser().getId();
        StudySet set = setTags.get(0).getStudySet();
        Long tagId1 = setTags.get(0).getTag().getId();
        Long tagId2 = setTags.get(1).getTag().getId();

        List<StudySet> sets = repository.getStudySetsByUserAndTags(userId, List.of(tagId1, tagId2));
        assertFalse(sets.isEmpty());
        assertEquals(set, sets.get(0));
    }

    @Test
    void delete() {
        SetTag setTag = insertData("get-all", 1).get(0);
        Long userId = setTag.getUser().getId();
        Long setId = setTag.getStudySet().getId();
        Long tagId = setTag.getTag().getId();

        assertTrue(repository.getSetTagById(userId, setId, tagId).isPresent());
        repository.delete(userId, setId, tagId);
        assertTrue(repository.getSetTagById(userId, setId, tagId).isEmpty());
    }
}
package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
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
class TagRepositoryImplTest {
    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private TagRepository tagRepository;
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
    private record InsertResult(User user, List<Tag> tags) {}
    private InsertResult insertData(String name, int count) {
        List<User> users = insertions.generateUsers(name, 1);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user = savedUsers.get(0);
        List<Tag> tags = insertions.generateTags(user, name, count);
        List<Tag> savedTags = insertions.saveAll(tagRepository, tags);
        return new InsertResult(user, savedTags);
    }

    private SoftDeletionTest.BeforeSoftDeletion<Tag, Long> soft() {
        return soft.using(tagRepository::deleteById, Tag::getId);
    }

    @Test
    void save() {
        InsertResult saved = insertData("saved", 2);
        assertEquals(2, saved.tags().size());
    }
    @Test
    void findAll() {
        InsertResult insertResult = insertData("find-all", 3);
        List<Tag> tags = insertResult.tags();
        List<Tag> all = tagRepository.findAll();
        MTest.containsAll(all, tags);

        Tag tag = tags.get(0);
        tagRepository.deleteById(tag.getId());
        List<Tag> allAfterDelete = tagRepository.findAll();
        assertFalse(allAfterDelete.contains(tag));
    }

    @Test
    void findById() {
        InsertResult insertResult = insertData("find-id", 1);
        Tag tag = insertResult.tags().get(0);
        Long id = tag.getId();
        Optional<Tag> tagFromDB = tagRepository.findById(id);
        assertTrue(tagFromDB.isPresent());
        assertEquals(tag, tagFromDB.get());

        tagRepository.deleteById(id);
        Optional<Tag> tagAfterDeleted = tagRepository.findById(id);
        assertTrue(tagAfterDeleted.isEmpty());

        Optional<Tag> notExisting = tagRepository.findById(Long.MAX_VALUE);
        assertTrue(notExisting.isEmpty());
    }

    @Test
    void deleteById() {
        InsertResult insertResult = insertData("delete", 1);
        Tag tag = insertResult.tags().get(0);
        Long id = tag.getId();
        assertTrue(tagRepository.findById(id).isPresent());
        tagRepository.deleteById(id);
        assertTrue(tagRepository.findById(id).isEmpty());
    }

    @Test
    void findAllIncludeDeleted() {
        InsertResult insertResult = insertData("all-include-deleted", 2);
        List<Tag> tags = insertResult.tags();
        soft().removeAll(tags)
                .byIds().validateAbsentIn(() -> tagRepository.findAll())
                .byIds().validatePresentIn(() -> tagRepository.findAllIncludeDeleted());
    }

    @Test
    void restoreById() {
        InsertResult insertResult = insertData("restore-deleted", 2);
        List<Tag> tags = insertResult.tags();
        soft().removeAll(tags)
                .byIds().validateAbsentIn(() -> tagRepository.findAll())
                .restoreWith(tagRepository::restoreById)
                .byIds().validatePresentIn(() -> tagRepository.findAll());
    }

    @Test
    void findByIdIncludeDeleted() {
        InsertResult insertResult = insertData("id-include-deleted", 1);
        Tag tag = insertResult.tags().get(0);
        Long id = tag.getId();

        Optional<Tag> opt1 = tagRepository.findByIdIncludeDeleted(id);
        assertTrue(opt1.isPresent());

        tagRepository.deleteById(id);
        Optional<Tag> opt2 = tagRepository.findByIdIncludeDeleted(id);
        assertTrue(opt2.isPresent());

        assertTrue(tagRepository.findByIdIncludeDeleted(Long.MAX_VALUE).isEmpty());
    }

    @Test
    void getTagsByOwner() {
        InsertResult insertResult = insertData("with-owner", 3);
        User owner = insertResult.user();
        List<Tag> tags = insertResult.tags();

        List<Tag> tagsByOwner = tagRepository.getTagsByOwner(owner.getId());
        MTest.compareUnordered(tags, tagsByOwner);

        tagRepository.deleteById(tags.get(0).getId());

        List<Tag> modified = tags.subList(1, tags.size());

        List<Tag> tagsByOwnerAfter = tagRepository.getTagsByOwner(owner.getId());
        MTest.compareUnordered(modified, tagsByOwnerAfter);
    }

    @Test
    void getTagsByOwnerAndName() {
        String specialName = "__SPECIAL_NAME__";
        InsertResult insertResult = insertData(specialName, 1);
        Long ownerId = insertResult.user().getId();
        Tag special = insertResult.tags().get(0);

        List<Tag> tagsByOwnerAndName = tagRepository.getTagsByOwnerAndName(ownerId, specialName);
        MTest.compareUnordered(insertResult.tags(), tagsByOwnerAndName);

        List<Tag> other = tagRepository.getTagsByOwnerAndName(ownerId, "Other name");
        assertTrue(other.isEmpty());
    }
}
package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.TagFactory;
import mouse.project.termverseweb.models.UserFactory;
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
class TagServiceImplTest {

    private final TagService tagService;
    private final UserService userService;
    private final Factories factories;
    private final SoftDeletionTest softDeletionTest;
    @Autowired
    public TagServiceImplTest(TagService tagService,
                              UserService userService,
                              Factories factories,
                              SoftDeletionTest softDeletionTest) {
        this.tagService = tagService;
        this.userService = userService;
        this.factories = factories;
        this.softDeletionTest = softDeletionTest;
    }
    private InsertedData insertTestData() {
        UserCreateDTO user = factories.getFactory(UserFactory.class).userCreateDTO("User");
        UserResponseDTO savedUser = userService.save(user);
        Long userId = savedUser.getId();

        List<String> tagNames = List.of("Easy", "Medium", "Hard");
        List<TagResponseDTO> savedTags = new ArrayList<>();
        TagFactory factory = factories.getFactory(TagFactory.class);
        for (String tagName : tagNames) {
            TagCreateDTO tag = factory.tagCreateDTO(userId, tagName);
            TagResponseDTO savedTag = tagService.save(tag);
            savedTags.add(savedTag);
        }

        return new InsertedData(savedUser, savedTags);
    }

    private record InsertedData(UserResponseDTO user, List<TagResponseDTO> tags) {
    }
    @Test
    void getAll() {
        InsertedData insertedData = insertTestData();
        int size = insertedData.tags().size();
        List<TagResponseDTO> all = tagService.getAll();
        assertEquals(size, all.size());
        softDeletionTest.using(tagService::removeById, TagResponseDTO::getId)
                .removeAll(insertedData.tags)
                .validateAbsentIn(tagService::getAll)
                .restoreWith(tagService::restoreById);
    }

    @Test
    void getById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void removeById() {
    }

    @Test
    void hardGet() {
    }

    @Test
    void getAllWithDeleted() {
    }

    @Test
    void getAllByUser() {
    }

    @Test
    void getAllByUserAndName() {
    }

    @Test
    void restoreById() {
    }
}
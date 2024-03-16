package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetUpdateDTO;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.UserFactory;
import mouse.project.termverseweb.models.UserStudySetFactory;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserStudySetServiceImplTest {

    private final UserStudySetService service;
    private final UserService userService;
    private final StudySetService studySetService;
    private final Factories factories;
    @Autowired
    public UserStudySetServiceImplTest(UserStudySetService service,
                                       UserService userService,
                                       StudySetService studySetService, Factories factories) {
        this.service = service;
        this.userService = userService;
        this.studySetService = studySetService;
        this.factories = factories;
    }
    private UserStudySetCreateDTO createRelation(Long userId, Long setId, String type) {
        return factories.getFactory(UserStudySetFactory.class).getCreateDTO(userId, setId, type);
    }

    private UserStudySetResponseDTO saveRelation(Long userId, Long setId, String type) {
        UserStudySetCreateDTO relation = createRelation(userId, setId, type);
        return service.save(relation);
    }

    private UserResponseDTO saveUser(String name) {
        UserCreateDTO user = factories.getFactory(UserFactory.class).userCreateDTO(name);
        return userService.save(user);
    }

    private StudySetResponseDTO saveStudySet(String name) {
        StudySetCreateDTO studySetCreateDTO = factories.getFactory(StudySetFactory.class).studySetCreateDTO(name);
        return studySetService.save(studySetCreateDTO);
    }
    @Test
    void save() {
        UserResponseDTO alice = saveUser("Alice");
        Long aliceId = alice.getId();
        StudySetResponseDTO set = saveStudySet("My first study set");
        Long setId = set.getId();

        UserStudySetResponseDTO saved = service.save(aliceId, setId, UserStudySetRelation.OWNER);
        Long id = saved.getId();
        assertNotNull(id);

        assertEquals(aliceId, saved.getUserId());
        assertEquals(setId, saved.getStudySetId());
        assertEquals(UserStudySetRelation.OWNER, saved.getType());

        UserStudySetResponseDTO byId = service.getById(id);
        assertEquals(saved, byId);

    }
    private UserStudySetResponseDTO saveRelation(String name) {
        UserResponseDTO bob = saveUser("name");
        Long bobId = bob.getId();
        StudySetResponseDTO set = saveStudySet(name + "'s set");
        Long setId = set.getId();

        return saveRelation(bobId, setId, UserStudySetRelation.OWNER);
    }
    @Test
    void update() {
        UserStudySetResponseDTO relation = saveRelation("Bob");

        UserStudySetUpdateDTO updateDTO = new UserStudySetUpdateDTO();
        Long id = relation.getId();
        updateDTO.setId(id);
        updateDTO.setUserId(relation.getUserId());
        updateDTO.setStudySetId(relation.getStudySetId());
        updateDTO.setType(UserStudySetRelation.EDITOR);

        UserStudySetResponseDTO updated = service.update(updateDTO);

        assertEquals(UserStudySetRelation.EDITOR, updated.getType());
    }

    @Test
    void getById() {
        UserResponseDTO bob = saveUser("Carl");
        Long bobId = bob.getId();
        StudySetResponseDTO set = saveStudySet("Carl's set");
        Long setId = set.getId();

        UserStudySetResponseDTO relation = saveRelation(bobId, setId, UserStudySetRelation.OWNER);
        Long id = relation.getId();
        UserStudySetResponseDTO relationById = service.getById(id);
        assertEquals(id, relationById.getId());

        Long notExisting = Long.MAX_VALUE;
        assertThrows(EntityNotFoundException.class, ()->service.getById(notExisting));
    }

    @Test
    void removeById() {
        UserStudySetResponseDTO relation = saveRelation("Elliott");
        Long id = relation.getId();
        service.removeById(id);
        assertThrows(EntityNotFoundException.class, ()->service.getById(id));
    }

    @Test
    void getAll() {
        UserStudySetResponseDTO denis = saveRelation("Denis");
        assertFalse(service.getAll().isEmpty());
        assertTrue(service.getAll().contains(denis));
    }
}
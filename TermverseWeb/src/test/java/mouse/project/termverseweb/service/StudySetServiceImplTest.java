package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.UserFactory;
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
class StudySetServiceImplTest {
    private final StudySetService studySetService;
    private final UserService userService;
    private final Factories factories;
    @Autowired
    public StudySetServiceImplTest(StudySetService studySetService, UserService userService, Factories factories) {
        this.studySetService = studySetService;
        this.userService = userService;
        this.factories = factories;
    }

    private UserResponseDTO saveUser(String userName) {
        UserCreateDTO userCreateDTO = factories.getFactory(UserFactory.class).userCreateDTO(userName);
        return userService.save(userCreateDTO);
    }

    private StudySetCreateDTO createStudySet(String setName) {
        return factories.getFactory(StudySetFactory.class).studySetCreateDTO(setName);
    }


    @Test
    void findAll() {
        StudySetCreateDTO studySet = createStudySet("Set-find-all-1");
        StudySetResponseDTO savedSet = studySetService.save(studySet);
        assertFalse(studySetService.findAll().isEmpty());
        assertTrue(studySetService.findAll().contains(savedSet));
    }

    @Test
    void save() {
        StudySetCreateDTO studySet = createStudySet("Save-1");
        StudySetResponseDTO savedSet = studySetService.save(studySet);
        Long id = savedSet.getId();
        assertNotNull(id);

        StudySetResponseDTO studySetResponse = studySetService.findById(id);
        assertNotNull(studySetResponse);
        assertNotNull(studySetResponse.getCreatedAt());

        StudySetCreateDTO studySet2 = createStudySet("Save-2");
        studySetService.save(studySet2);
        StudySetResponseDTO savedSet2 = studySetService.save(studySet);
        Long id2 = savedSet2.getId();
        assertNotNull(id2);
        assertNotEquals(id, id2);
    }

    @Test
    void findById() {

    }
}
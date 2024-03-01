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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StudySetServiceImplTest {
    private final StudySetService studySetService;
    private final UserStudySetService userStudySetService;
    private final UserService userService;
    private final Factories factories;
    @Autowired
    public StudySetServiceImplTest(StudySetService studySetService,
                                   UserStudySetService userStudySetService,
                                   UserService userService,
                                   Factories factories) {
        this.studySetService = studySetService;
        this.userStudySetService = userStudySetService;
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

    private StudySetResponseDTO saveStudySet(String setName) {
        return studySetService.save(createStudySet(setName));
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

    @Test
    void findStudySetsByUser() {
        UserResponseDTO annaResponse = saveUser("Anna");
        UserResponseDTO benResponse = saveUser("Ben");

        Long annaId = annaResponse.getId();
        Long benId = benResponse.getId();

        StudySetResponseDTO studySet1 = saveStudySet("find-study-set-by-user-1-Anna");
        StudySetResponseDTO studySet2 = saveStudySet("find-study-set-by-user-2-Ben");
        StudySetResponseDTO studySet3 = saveStudySet("find-study-set-by-user-3-AlsoBen");
        StudySetResponseDTO studySet4 = saveStudySet("find-study-set-by-user-4-Shared");

        userStudySetService.save(annaId, studySet1.getId(), "owner");
        userStudySetService.save(benId, studySet2.getId(), "owner");
        userStudySetService.save(benId, studySet3.getId(), "owner");

        userStudySetService.save(annaId, studySet4.getId(), "owner");
        userStudySetService.save(benId, studySet4.getId(), "viewer");

        List<StudySetResponseDTO> annaStudySets = studySetService.findStudySetsByUser(annaId);
        List<StudySetResponseDTO> benStudySets = studySetService.findStudySetsByUser(benId);
        assertEquals(2, annaStudySets.size());
        assertTrue(annaStudySets.containsAll(List.of(studySet1, studySet4)));
        assertEquals(3, benStudySets.size());
        assertTrue(benStudySets.containsAll(List.of(studySet2, studySet3, studySet4)));

        studySetService.deleteById(studySet4.getId());

        List<StudySetResponseDTO> benStudySetsAfterDeletion = studySetService.findStudySetsByUser(benId);

        assertEquals(2, benStudySetsAfterDeletion.size());
        assertTrue(benStudySets.containsAll(List.of(studySet2, studySet3)));

        userService.removeById(benId);

        assertTrue(studySetService.findStudySetsByUser(benId).isEmpty());

    }
}
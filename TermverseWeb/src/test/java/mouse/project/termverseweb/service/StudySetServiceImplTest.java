package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.UserFactory;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.utils.DateUtils;
import mouse.project.termverseweb.utils.UserStudySetRelation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private final SoftDeletionTest softDeletionTest;
    @Autowired
    public StudySetServiceImplTest(StudySetService studySetService,
                                   UserStudySetService userStudySetService,
                                   UserService userService,
                                   Factories factories, SoftDeletionTest softDeletionTest) {
        this.studySetService = studySetService;
        this.userStudySetService = userStudySetService;
        this.userService = userService;
        this.factories = factories;
        this.softDeletionTest = softDeletionTest;
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
        assertEquals("Save-1", savedSet.getName());

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
        StudySetCreateDTO studySet = createStudySet("Find-by-id-1");
        StudySetResponseDTO savedSet = studySetService.save(studySet);
        Long id = savedSet.getId();
        assertNotNull(id);

        StudySetResponseDTO byId = studySetService.findById(id);
        assertEquals(id, byId.getId());
        assertEquals(savedSet, byId);
    }

    @Test
    void updateById() {
        StudySetResponseDTO studySet = saveStudySet("Update-1");
        Long id = studySet.getId();
        String newName = "New name";
        StudySetUpdateDTO updateDTO = factories.
                getFactory(StudySetFactory.class).
                studySetUpdateDTO(id, newName);

        StudySetResponseDTO updated = studySetService.update(updateDTO);
        assertEquals(newName, updated.getName());

        StudySetResponseDTO byId = studySetService.findById(id);
        assertEquals(newName, byId.getName());
    }

    @Test
    void testTimePeriods() {
        StudySetCreateDTO createDTO1 = createStudySet("Update-1");
        LocalDateTime customTime = DateUtils.fromString("2010-01-10 10:00:00");
        StudySetResponseDTO response1 = studySetService.saveWithCustomTime(createDTO1, customTime);

        StudySetCreateDTO createDTO2 = createStudySet("Update-2");
        LocalDateTime customTime2 = DateUtils.fromString("2010-01-11 10:00:00");
        StudySetResponseDTO response2 = studySetService.saveWithCustomTime(createDTO2, customTime2);

        List<StudySetResponseDTO> inJanuary2010 = studySetService.findAllByCreatedDateRange(
                DateUtils.fromString("2010-01-01 10:00:00"),
                DateUtils.fromString("2010-02-01 10:00:00"));

        assertEquals(2, inJanuary2010.size());
        assertTrue(inJanuary2010.containsAll(List.of(response1, response2)));

        List<StudySetResponseDTO> inGivenTime = studySetService.findAllByCreatedDateRange(
                customTime,
                customTime.plus(Duration.ofSeconds(10)));

        assertEquals(1, inGivenTime.size());
        assertTrue(inJanuary2010.contains(response1));
    }

    @Test
    void testSoftDeletion() {
        StudySetResponseDTO studySet1 = saveStudySet("Soft delete 1");
        Long id = studySet1.getId();
        assertTrue(studySetService.findAll().contains(studySet1));

        softDeletionTest.remove(studySet1)
                .using(studySetService::deleteById, StudySetResponseDTO::getId)
                .validateAllAbsentIn(studySetService.findAll())
                .validateAllPresentIn(studySetService.findAllIncludeDeleted())
                .validateThrows(EntityNotFoundException.class, () -> studySetService.findById(id))
                .restoreWith(studySetService::restoreById);
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

        userStudySetService.save(annaId, studySet1.getId(), UserStudySetRelation.OWNER);
        userStudySetService.save(benId, studySet2.getId(), UserStudySetRelation.OWNER);
        userStudySetService.save(benId, studySet3.getId(), UserStudySetRelation.OWNER);

        userStudySetService.save(annaId, studySet4.getId(), UserStudySetRelation.OWNER);
        userStudySetService.save(benId, studySet4.getId(), UserStudySetRelation.VIEWER);

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
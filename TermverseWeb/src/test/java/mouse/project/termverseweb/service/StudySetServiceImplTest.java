package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetUpdateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetWithTermsResponseDTO;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.StudySetFactory;
import mouse.project.termverseweb.models.TermFactory;
import mouse.project.termverseweb.models.UserFactory;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.utils.DateUtils;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

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
    private final TermService termService;
    @Autowired
    public StudySetServiceImplTest(StudySetService studySetService,
                                   UserStudySetService userStudySetService,
                                   UserService userService,
                                   Factories factories,
                                   SoftDeletionTest softDeletionTest,
                                   TermService termService) {
        this.studySetService = studySetService;
        this.userStudySetService = userStudySetService;
        this.userService = userService;
        this.factories = factories;
        this.softDeletionTest = softDeletionTest;
        this.termService = termService;
    }

    private UserResponseDTO saveUser(String userName) {
        UserCreateDTO userCreateDTO = factories.getFactory(UserFactory.class).userCreateDTO(userName);
        return userService.save(userCreateDTO);
    }
    private StudySetCreateDTO createStudySet(String setName) {
        return factories.getFactory(StudySetFactory.class).studySetCreateDTO(setName);
    }
    private TermCreateDTO createTerm(String name, int order) {
        return factories.getFactory(TermFactory.class).termCreateDTO(name, order);
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

        softDeletionTest.using(studySetService::deleteById, StudySetResponseDTO::getId)
                .remove(byId).validateThrows(EntityNotFoundException.class, ()->studySetService.update(updateDTO));
    }

    @Test
    void testTimePeriods() {
        StudySetCreateDTO createDTO1 = createStudySet("Update-1");
        LocalDateTime customTime = DateUtils.fromString("2010-01-10 10:00:00");
        StudySetResponseDTO response1 = studySetService.saveWithCustomTime(createDTO1, customTime);

        StudySetCreateDTO createDTO2 = createStudySet("Update-2");
        LocalDateTime customTime2 = DateUtils.fromString("2010-01-11 10:00:00");
        StudySetResponseDTO response2 = studySetService.saveWithCustomTime(createDTO2, customTime2);

        Supplier<List<StudySetResponseDTO>> getInRange = () -> studySetService.findAllByCreatedDateRange(
                DateUtils.fromString("2010-01-01 10:00:00"),
                DateUtils.fromString("2010-02-01 10:00:00"));

        List<StudySetResponseDTO> inJanuary2010 = getInRange.get();

        assertEquals(2, inJanuary2010.size());
        assertTrue(inJanuary2010.containsAll(List.of(response1, response2)));

        List<StudySetResponseDTO> inGivenTime = studySetService.findAllByCreatedDateRange(
                customTime,
                customTime.plus(Duration.ofSeconds(10)));

        assertEquals(1, inGivenTime.size());
        assertTrue(inGivenTime.contains(response1));
        softDeletionTest.using(studySetService::deleteById, StudySetResponseDTO::getId)
                .removeAll(List.of(response1, response2))
                .validateAbsentIn(getInRange)
                .restoreWith(studySetService::restoreById);
    }

    @Test
    void testSoftDeletion() {
        StudySetResponseDTO studySet1 = saveStudySet("Soft delete 1");
        Long id = studySet1.getId();
        assertTrue(studySetService.findAll().contains(studySet1));

        softDeletionTest.using(studySetService::deleteById, StudySetResponseDTO::getId)
                .remove(studySet1)
                .validateAbsentIn(studySetService::findAll)
                .validatePresentIn(studySetService::findAllIncludeDeleted)
                .validateThrows(EntityNotFoundException.class, () -> studySetService.findById(id))
                .restoreWith(studySetService::restoreById);

        assertTrue(studySetService.findAll().contains(studySet1));
    }

    @Test
    void testSearchByName() {
        StudySetResponseDTO set1 = saveStudySet("Penguin");
        StudySetResponseDTO set2 = saveStudySet("Lion");
        List<StudySetResponseDTO> both = List.of(set1, set2);

        assertTrue(studySetService.findAllByNameIgnoreCase("pen").contains(set1));
        assertTrue(studySetService.findAllByNameIgnoreCase("lion").contains(set2));
        assertTrue(studySetService.findAllByNameIgnoreCase("N").containsAll(both));

        assertFalse(studySetService.findAllByNameIgnoreCase("Lion").containsAll(both));
        assertFalse(studySetService.findAllByNameIgnoreCase("Lion").contains(set1));
        assertFalse(studySetService.findAllByNameIgnoreCase("Penguin").contains(set2));


        softDeletionTest.using(studySetService::deleteById, StudySetResponseDTO::getId)
                .removeAll(both)
                .validateAbsentIn(() -> studySetService.findAllByNameIgnoreCase("N"));

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
    @Test
    void testSavingWithTerms() {
        StudySetCreateDTO studySet = createStudySet("Set with terms 1");
        studySet.setTerms(List.of(createTerm("T1", 1), createTerm("T2", 2)));

        StudySetResponseDTO studySetSaved = studySetService.save(studySet);
        Long id = studySetSaved.getId();
        StudySetWithTermsResponseDTO byIdWithTerms = studySetService.findByIdWithTerms(id);
        assertEquals(2, byIdWithTerms.getTerms().size());
        System.out.println(byIdWithTerms.getTerms());

        softDeletionTest.using(termService::removeById, TermResponseDTO::getId)
                .removeAll(byIdWithTerms.getTerms())
                .validate(() -> {
                    List<TermResponseDTO> terms = studySetService.findByIdWithTerms(id).getTerms();
                    System.out.println(terms);
                    assertEquals(0, terms.size());
                });

    }




}
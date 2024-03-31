package mouse.project.termverseweb.service;

import jakarta.persistence.EntityNotFoundException;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.settag.SetTagResponseDTO;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.tag.TagCreateDTO;
import mouse.project.termverseweb.dto.tag.TagResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SetTagServiceImplTest {

    private final SetTagService service;
    private final UserStudySetService userStudySetService;
    private final UserService userService;
    private final StudySetService studySetService;
    private final TagService tagService;
    private final Factories factories;
    @Autowired
    SetTagServiceImplTest(SetTagService service,
                          UserStudySetService userStudySetService,
                          UserService userService,
                          StudySetService studySetService,
                          TagService tagService,
                          Factories factories) {
        this.service = service;
        this.userStudySetService = userStudySetService;
        this.userService = userService;
        this.studySetService = studySetService;
        this.tagService = tagService;
        this.factories = factories;
    }

    private record InsertionResult(List<UserResponseDTO> users,
                                   Map<Long, List<StudySetResponseDTO>> personal,
                                   List<StudySetResponseDTO> mutualSets,
                                   List<UserStudySetResponseDTO> relations,
                                   Map<Long, List<TagResponseDTO>> tags) {
        public static InsertionResult instance() {
            return new InsertionResult(
                    new ArrayList<>(),
                    new HashMap<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );
        }
    }
    private InsertionResult insertData(String name) {
        InsertionResult result = InsertionResult.instance();
        int userCount = 2;
        int personalSets = 2;
        int mutualSets = 2;
        int tags = 3;

        insertUsers(result, name, userCount);
        insertStudySetsPersonal(result, result.users(), personalSets);
        insertStudySetsMutual(result, result.users(), mutualSets);
        insertTags(result, result.users(), tags);
        return result;
    }

    private void insertUsers(InsertionResult result, String name, int count) {
        UserFactory userFactory = factories.getFactory(UserFactory.class);
        for (int i = 0; i < count; i++) {
            UserCreateDTO user = userFactory.userCreateDTO(name + i);
            UserResponseDTO saved = userService.save(user);
            result.users().add(saved);
        }
    }

    private void insertStudySetsPersonal(InsertionResult result, List<UserResponseDTO> users, int count) {
        StudySetFactory studySetFactory = factories.getFactory(StudySetFactory.class);
        users.forEach(u -> {
            String name = u.getName() + "'s set ";
            for (int i = 0; i < count; i++) {
                char ch = (char) ('A' + i);
                StudySetCreateDTO studySetCreateDTO = studySetFactory.studySetCreateDTO(name + ch);
                StudySetResponseDTO saved = studySetService.save(studySetCreateDTO);
                List<StudySetResponseDTO> list = result.personal.computeIfAbsent(u.getId(), us -> new ArrayList<>());
                list.add(saved);
                UserStudySetResponseDTO rel = userStudySetService.save(u.getId(), saved.getId(),
                        UserStudySetRelation.OWNER);
                result.relations().add(rel);
            }
        });

    }

    private void insertStudySetsMutual(InsertionResult result, List<UserResponseDTO> users, int count) {
        StudySetFactory studySetFactory = factories.getFactory(StudySetFactory.class);
        List<StudySetResponseDTO> mutualList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StudySetCreateDTO studySetCreateDTO = studySetFactory.studySetCreateDTO("Mutual " + i);
            StudySetResponseDTO saved = studySetService.save(studySetCreateDTO);
            result.mutualSets().add(saved);
            mutualList.add(saved);
        }
        users.forEach(u -> {
           mutualList.forEach(m -> {
               UserStudySetResponseDTO rel = userStudySetService.save(u.getId(), m.getId(),
                       UserStudySetRelation.VIEWER);
               result.relations().add(rel);
           });

        });

    }

    private void insertTags(InsertionResult result, List<UserResponseDTO> users, int count) {
        TagFactory tagFactory = factories.getFactory(TagFactory.class);
        for (UserResponseDTO user : users) {
            for (int i = 0; i < count; i++) {
                String name = user.getName() + "-" + (char) ('A' + i);
                TagCreateDTO createDTO = tagFactory.tagCreateDTO(user.getId(), name);
                TagResponseDTO saved = tagService.save(createDTO);
                List<TagResponseDTO> list = result.tags().computeIfAbsent(user.getId(), u -> new ArrayList<>());
                list.add(saved);
            }
        }
    }

    private StudySetResponseDTO anyMutual(InsertionResult result) {
        List<StudySetResponseDTO> list = result.mutualSets();
        assert !list.isEmpty();
        return list.get(0);
    }
    private StudySetResponseDTO anyPersonal(InsertionResult result, Long userId) {
        List<StudySetResponseDTO> list = result.personal().get(userId);
        assert list != null;
        assert !list.isEmpty();
        return list.get(0);
    }

    private TagResponseDTO anyTag(InsertionResult result, Long userId) {
        List<TagResponseDTO> list = userTags(result, userId);
        assert !list.isEmpty();
        return list.get(0);
    }

    private List<TagResponseDTO> userTags(InsertionResult result, Long id) {
        List<TagResponseDTO> list = result.tags().get(id);
        assert list != null;
        return list;
    }

    private UserResponseDTO anyUser(InsertionResult result) {
        assert !result.users().isEmpty();
        return result.users().get(0);
    }

    @Test
    void save() {
        InsertionResult result = insertData("Saving");
        UserResponseDTO user = anyUser(result);
        StudySetResponseDTO set = anyMutual(result);
        Long userId = user.getId();
        TagResponseDTO tag = anyTag(result, userId);
        Long setId = set.getId();
        Long tagId = tag.getId();
        SetTagResponseDTO saved = service.save(userId, setId, tagId);
        assertTrue(service.getAll().contains(saved));
        SetTagResponseDTO byId = service.getSetTagById(userId, setId, tagId);

        assertEquals(saved, byId);
        assertEquals(userId, byId.getUserId());
        assertEquals(setId, byId.getStudySetId());
        assertEquals(tagId, byId.getTagId());
    }


    @Test
    void getSetTagById() {
        InsertionResult result = insertData("Getting");
        UserResponseDTO user = anyUser(result);
        StudySetResponseDTO set = anyMutual(result);
        Long userId = user.getId();
        TagResponseDTO tag = anyTag(result, userId);
        Long setId = set.getId();
        Long tagId = tag.getId();
        service.save(userId, setId, tagId);
        SetTagResponseDTO byId = service.getSetTagById(userId, setId, tagId);
        assertNotNull(byId);
        assertThrows(EntityNotFoundException.class, () -> service.getSetTagById(Long.MAX_VALUE, setId, tagId));
        assertThrows(EntityNotFoundException.class, () -> service.getSetTagById(userId, Long.MAX_VALUE, tagId));
        assertThrows(EntityNotFoundException.class, () -> service.getSetTagById(userId, Long.MAX_VALUE, tagId));
    }

    @Test
    void getStudySetsByUserAndTags_Simple() {
        InsertionResult insertion = insertData("Simple");
        UserResponseDTO user = anyUser(insertion);
        StudySetResponseDTO set = anyMutual(insertion);
        Long userId = user.getId();
        TagResponseDTO tag = anyTag(insertion, userId);
        Long setId = set.getId();
        Long tagId = tag.getId();
        service.save(userId, setId, tagId);
        List<StudySetResponseDTO> result = service.getStudySetsByUserAndTags(userId, List.of(tagId));
        assertEquals(1, result.size());
        assertEquals(set, result.get(0));
    }

    @Test
    void getStudySetsByUserAndTags_Both() {
        InsertionResult insertion = insertData("Both");
        UserResponseDTO user = anyUser(insertion);
        StudySetResponseDTO set = anyMutual(insertion);
        Long userId = user.getId();
        List<TagResponseDTO> tags = userTags(insertion, userId);
        assert tags.size() > 1;
        TagResponseDTO tag1 = tags.get(0);
        TagResponseDTO tag2 = tags.get(1);
        service.save(userId, set.getId(), tag1.getId());
        List<StudySetResponseDTO> withBoth = service.getStudySetsByUserAndTags(
                userId, List.of(tag1.getId(), tag2.getId())
        );
        assertTrue(withBoth.isEmpty());
        List<StudySetResponseDTO> withFirst = service.getStudySetsByUserAndTags(userId, List.of(tag1.getId()));
        assertEquals(1, withFirst.size());
        assertEquals(set, withFirst.get(0));

        List<StudySetResponseDTO> withSecond = service.getStudySetsByUserAndTags(userId, List.of(tag2.getId()));
        assertTrue(withSecond.isEmpty());
    }

    @Test
    void delete() {
    }
}
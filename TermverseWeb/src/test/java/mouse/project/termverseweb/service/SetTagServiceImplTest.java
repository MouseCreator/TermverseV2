package mouse.project.termverseweb.service;

import mouse.project.termverseweb.defines.UserStudySetRelation;
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
import java.util.List;

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
                                   List<StudySetResponseDTO> studySets,
                                   List<UserStudySetResponseDTO> relations,
                                   List<TagResponseDTO> tags) {
        public static InsertionResult instance() {
            return new InsertionResult(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }
    }
    private InsertionResult insertData(String name) {
        InsertionResult result = InsertionResult.instance();
        insertUsers(result, name, 2);
        insertStudySetsPersonal(result, result.users(), 2);
        insertStudySetsMutual(result, result.users(), 2);
        insertTags(result, result.users(), 3);
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
                result.studySets().add(saved);

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
            result.studySets().add(saved);
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
                result.tags().add(saved);
            }
        }
    }

    private void addRelation(InsertionResult result, Long user, Long studySet) {

    }
    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void getSetTagById() {
    }

    @Test
    void getStudySetsByUserAndTags() {
    }

    @Test
    void delete() {
    }
}
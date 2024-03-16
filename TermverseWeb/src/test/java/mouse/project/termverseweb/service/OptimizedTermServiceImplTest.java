package mouse.project.termverseweb.service;

import lombok.Data;
import mouse.project.termverseweb.defines.Progress;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.dto.progress.TermProgressPair;
import mouse.project.termverseweb.dto.progress.TermProgressUpdates;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetResponseDTO;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermWithProgressResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetCreateDTO;
import mouse.project.termverseweb.dto.userstudyset.UserStudySetResponseDTO;
import mouse.project.termverseweb.models.*;
import org.springframework.context.ApplicationContext;
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
class OptimizedTermServiceImplTest {

    private final ApplicationContext context;
    private final OptimizedTermService optimizedTermService;
    private final Factories factories;

    private static final int TERMS_CREATED = 5;
    @Autowired
    public OptimizedTermServiceImplTest(ApplicationContext applicationContext,
                                        OptimizedTermService optimizedTermService,
                                        Factories factories) {
        this.context = applicationContext;
        this.optimizedTermService = optimizedTermService;
        this.factories = factories;
    }
    @Data
    private static class InsertedData {
        private UserResponseDTO user = null;
        private StudySetResponseDTO studySet = null;
        private UserStudySetResponseDTO userSet = null;
        private List<TermResponseDTO> terms = null;
    }
    private InsertedData insertData(String keyword) {
        UserService userService = context.getBean(UserService.class);
        InsertedData insertedData = new InsertedData();
        UserCreateDTO user = factories.getFactory(UserFactory.class).userCreateDTO(keyword);
        UserResponseDTO savedUser = userService.save(user);
        insertedData.setUser(savedUser);

        TermFactory termFactory = factories.getFactory(TermFactory.class);
        List<TermCreateDTO> terms = new ArrayList<>();
        for (int i = 1; i < 1 + TERMS_CREATED; i++ ){
            TermCreateDTO term = termFactory.termCreateDTO(keyword + "'s term " + i, i);
            terms.add(term);
        }

        StudySetService studySetService = context.getBean(StudySetService.class);
        StudySetCreateDTO set = factories.getFactory(StudySetFactory.class)
                .studySetCreateDTO(keyword + "'s study set");
        set.setTerms(terms);
        StudySetResponseDTO setSaved = studySetService.save(set);
        insertedData.setTerms(studySetService.findByIdWithTerms(setSaved.getId()).getTerms());
        insertedData.setStudySet(setSaved);


        UserStudySetService userStudySetService = context.getBean(UserStudySetService.class);
        UserStudySetCreateDTO relation = factories.getFactory(UserStudySetFactory.class).getCreateDTO(savedUser.getId(),
                setSaved.getId(), UserStudySetRelation.OWNER);
        UserStudySetResponseDTO relationSaved = userStudySetService.save(relation);
        insertedData.setUserSet(relationSaved);
        return insertedData;
    }
    @Test
    void updateAll() {
        InsertedData insertedData = insertData("Alice");
        TermProgressUpdates updates = generateUpdates(insertedData);
        List<TermWithProgressResponseDTO> initialProgress =
                optimizedTermService.initializeProgress(
                        insertedData.getUser().getId(),
                        insertedData.getStudySet().getId());
        optimizedTermService.updateAll(updates);
    }

    private TermProgressUpdates generateUpdates(InsertedData insertedData) {
        List<TermResponseDTO> terms = insertedData.getTerms();
        Long userId = insertedData.getUser().getId();
        TermProgressUpdates updates = new TermProgressUpdates();
        boolean studied = true;
        List<TermProgressPair> pairs = new ArrayList<>();
        for (TermResponseDTO term : terms) {
            TermProgressPair pair = new TermProgressPair(term.getId(), studied ? Progress.LEARNED : Progress.FAMILIAR);
            pairs.add(pair);
            studied = !studied;
        }
        updates.setUpdatesList(pairs);
        updates.setUserId(userId);
        return updates;
    }


    @Test
    void getForUserFromStudySet() {
    }

    @Test
    void initializeProgress() {
        InsertedData insertedData = insertData("Carl");
        List<TermWithProgressResponseDTO> initialProgress =
                optimizedTermService.initializeProgress(
                        insertedData.getUser().getId(),
                        insertedData.getStudySet().getId());
        assertEquals(TERMS_CREATED, initialProgress.size());
        for (TermWithProgressResponseDTO dto : initialProgress) {
            assertEquals(dto.getProgress(), Progress.UNFAMILIAR);
        }
    }

    @Test
    void resetProgress() {
    }

    @Test
    void removeProgress() {
    }
}
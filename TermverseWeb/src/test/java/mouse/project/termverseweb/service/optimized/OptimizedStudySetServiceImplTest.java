package mouse.project.termverseweb.service.optimized;

import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.dto.studyset.StudySetCreateDTO;
import mouse.project.termverseweb.dto.studyset.StudySetWithTermsResponseDTO;
import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.user.UserCreateDTO;
import mouse.project.termverseweb.dto.user.UserResponseDTO;
import mouse.project.termverseweb.models.Factories;

import mouse.project.termverseweb.models.TermFactory;
import mouse.project.termverseweb.models.UserFactory;
import mouse.project.termverseweb.service.TermService;
import mouse.project.termverseweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class OptimizedStudySetServiceImplTest {

    private final OptimizedStudySetService service;
    private final Factories factories;
    private final ApplicationContext context;
    @Autowired
    public OptimizedStudySetServiceImplTest(OptimizedStudySetService optimizedStudySetService,
                                     Factories factories,
                                     ApplicationContext context) {
        this.service = optimizedStudySetService;
        this.factories = factories;
        this.context = context;
    }

    private SetUserPair createData(String base) {
        UserCreateDTO user = factories.getFactory(UserFactory.class).userCreateDTO(base);
        List<TermCreateDTO> termResponseDTOS = generateTerms();
        UserResponseDTO userResponse = generateUserAndSave(user);
        return withCreator(userResponse, termResponseDTOS);
    }

    private List<SetUserPair> createData(String base, int count) {
        UserCreateDTO user = factories.getFactory(UserFactory.class).userCreateDTO(base);
        UserResponseDTO userResponse = generateUserAndSave(user);
        List<SetUserPair> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<TermCreateDTO> termResponseDTOS = generateTerms();
            SetUserPair dto = withCreator(userResponse, termResponseDTOS);
            result.add(dto);
        }
        return result;
    }

    private StudySetWithTermsResponseDTO insertData(SetUserPair dto) {
        return service.create(dto.user().getId(),dto.set());
    }

    private List<StudySetWithTermsResponseDTO> insertData(List<SetUserPair> dtos) {
        return dtos.stream().map(this::insertData).toList();
    }

    private UserResponseDTO generateUserAndSave(UserCreateDTO user) {
        UserService userService = context.getBean(UserService.class);
        return userService.save(user);
    }

    private List<TermCreateDTO> generateTerms() {
        TermFactory termFactory = factories.getFactory(TermFactory.class);
        int tCount = 4;
        List<TermCreateDTO> list = new ArrayList<>();
        for (int i = 1; i < tCount+1; i++) {
            TermCreateDTO termCreateDTO = termFactory.termCreateDTO("Term " + i, i);
            list.add(termCreateDTO);
        }
        return list;
    }
    private record SetUserPair(UserResponseDTO user, StudySetCreateDTO set) {}
    private SetUserPair withCreator(UserResponseDTO createdBy, List<TermCreateDTO> terms) {
        StudySetCreateDTO dto = new StudySetCreateDTO();
        dto.setTerms(terms);
        String name = createdBy.getName() + "'s study set";
        dto.setName(name);
        dto.setPictureUrl("//" + name + "//");
        return new SetUserPair(createdBy, dto);
    }
    @Test
    void create() {
        int count = 2;
        List<SetUserPair> data = createData("to-create", count);
        List<StudySetWithTermsResponseDTO> saved = insertData(data);
        assertEquals(data.size(), saved.size());

        StudySetWithTermsResponseDTO first = saved.get(0);

        Long setId = first.getId();
        TermService termService = context.getBean(TermService.class);
        List<TermResponseDTO> termsFromDB = termService.getByStudySet(setId);
        MTest.compareUnordered(first.getTerms(), termsFromDB);
    }
    @Test
    void getShortDescription() {
    }

    @Test
    void getStudySetsByUser() {
    }

    @Test
    void testGetStudySetsByUser() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void getHeader() {
    }
}
package mouse.project.termverseweb.service;

import lombok.RequiredArgsConstructor;
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
    @Autowired
    public StudySetServiceImplTest(StudySetService studySetService) {
        this.studySetService = studySetService;
    }
    @Test
    void findAll() {
        assertTrue(studySetService.findAll().isEmpty());
    }
}
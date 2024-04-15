package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SetTagRepositoryImplTest {
    @InitBeforeEach
    private SetTagRepository setTagRepository;

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }

    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
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
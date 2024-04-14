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
class UserRepositoryImplTest {
    @InitBeforeEach
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    private void insertData() {

    }
    @Test
    void findAll() {
        insertData();
        userRepository.findAll();
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void save() {
    }

    @Test
    void findAllIncludeDeleted() {
    }

    @Test
    void restoreById() {
    }

    @Test
    void findByIdIncludeDeleted() {
    }

    @Test
    void findAllByNameIgnoreCase() {
    }
}
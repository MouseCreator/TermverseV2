package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.models.Factories;
import mouse.project.termverseweb.models.UserFactory;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserRepositoryImplTest {
    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private Factories factories;
    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    private List<User> insertData(String base, int values) {
        UserFactory userFactory = factories.getFactory(UserFactory.class);
        List<User> result = new ArrayList<>();
        for (int i = 0; i < values; i++) {
            User user = userFactory.user(base + (i + 1));
            User saved = userRepository.save(user);
            result.add(saved);
        }
        return result;

    }
    private List<User> insertData(String base) {
        return insertData(base, 3);
    }
    @Test
    void findAll() {
        List<User> users = insertData("FA");
        List<User> all = userRepository.findAll();
        assertTrue(all.containsAll(users));
    }

    @Test
    void findById() {
        List<User> users = insertData("FA", 1);
        User user = users.get(0);
        Long id = user.getId();
        Optional<User> uOptional = userRepository.findById(id);
        assertTrue(uOptional.isPresent());
        assertEquals(user, uOptional.get());
    }

    @Test
    void deleteById() {
    }

    @Test
    void save() {
        List<User> savedUsers = insertData("SAVE", 4);
        assertEquals(4, savedUsers.size());
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
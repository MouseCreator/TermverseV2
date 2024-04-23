package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserMapping;
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
class UserMappingRepositoryImplTest {

    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private UserMappingRepository userMappingRepository;
    @InitBeforeEach
    private Insertions insertions;

    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }
    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }

    private User saveUser(String base) {
        List<User> users = insertions.generateUsers(base, 1);
        return userRepository.save(users.get(0));
    }

    private List<User> saveUsers(String base, int count) {
        List<User> users = insertions.generateUsers(base, count);
        insertions.saveAll(userRepository, users);
        return users;
    }
    @Test
    void save() {
        User user = saveUser("save-user");
        String key = "SEC-" + System.nanoTime();
        UserMapping userMapping = new UserMapping(key, user);
        UserMapping save = userMappingRepository.save(userMapping);
        assertEquals(key, save.getId());
        assertEquals(user, save.getUser());
    }

    @Test
    void findAll() {
        List<User> users = saveUsers("find-all", 3);
        List<UserMapping> mappings = new ArrayList<>();
        users.forEach(u -> {
            String key = "SEC-" + System.nanoTime();
            UserMapping userMapping = new UserMapping(key, u);
            mappings.add(userMapping);
            userMappingRepository.save(userMapping);
        });
        List<UserMapping> all = userMappingRepository.findAll();
        MTest.containsAll(all, mappings);
    }

    @Test
    void findById() {
        User user = saveUser("find-user");
        String key = "SEC-" + System.nanoTime();
        UserMapping userMapping = new UserMapping(key, user);
        userMappingRepository.save(userMapping);
        Optional<UserMapping> byId = userMappingRepository.findById(key);
        assertTrue(byId.isPresent());
        assertEquals(user, byId.get().getUser());

        userRepository.deleteById(user.getId());
        Optional<UserMapping> byId2 = userMappingRepository.findById(key);
        assertTrue(byId2.isEmpty());
    }

    @Test
    void deleteById() {
        User user = saveUser("delete-user");
        String key = "SEC-" + System.nanoTime();
        UserMapping userMapping = new UserMapping(key, user);
        userMappingRepository.save(userMapping);
        userMappingRepository.deleteById(key);

        assertTrue(userMappingRepository.findById(key).isEmpty());

    }


}
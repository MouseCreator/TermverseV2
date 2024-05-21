package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private Insertions insertions;
    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    private List<User> insertData(String base, int values) {
        List<User> users = insertions.generateUsers(base, values);
        return insertions.saveAll(userRepository, users);
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
        User user = users.getFirst();
        Long id = user.getId();
        Optional<User> uOptional = userRepository.findById(id);
        assertTrue(uOptional.isPresent());
        assertEquals(user, uOptional.get());

        Optional<User> noUser = userRepository.findById(Long.MAX_VALUE);
        assertTrue(noUser.isEmpty());
    }

    @Test
    void deleteById() {
        List<User> users = insertData("DEL", 1);
        User user = users.getFirst();
        Long id = user.getId();
        assertTrue(userRepository.findById(id).isPresent());
        userRepository.deleteById(id);
        assertTrue(userRepository.findById(id).isEmpty());
    }

    @Test
    void save() {
        List<User> savedUsers = insertData("SAVE", 4);
        assertEquals(4, savedUsers.size());
    }

    @Test
    void findAllIncludeDeleted() {
        List<User> users = insertData("F_A_I_D", 1);
        User user = users.getFirst();
        Long id = user.getId();
        userRepository.deleteById(id);
        List<User> list = userRepository.findAllIncludeDeleted();
        List<Long> ids = list.stream().map(User::getId).toList();
        assertTrue(ids.contains(user.getId()));
    }

    @Test
    void restoreById() {
        List<User> users = insertData("DEL", 1);
        User user = users.getFirst();
        Long id = user.getId();
        userRepository.deleteById(id);
        assertTrue(userRepository.findById(id).isEmpty());
        userRepository.restoreById(id);
        assertTrue(userRepository.findById(id).isPresent());
    }

    @Test
    void findByIdIncludeDeleted() {
        List<User> users = insertData("FIND_DEL", 1);
        User user = users.getFirst();
        Long id = user.getId();
        assertTrue(userRepository.findByIdIncludeDeleted(id).isPresent());
        userRepository.deleteById(id);
        assertTrue(userRepository.findByIdIncludeDeleted(id).isPresent());
    }

    @Test
    void findAllByNameIgnoreCase() {
        String specialSubs = "__SPECIAL_NAME__";
        String lowerCase = specialSubs.toLowerCase();
        List<User> users = insertData(specialSubs, 2);
        List<User> allSpecial = userRepository.findAllByNameIgnoreCase(lowerCase);
        assertEquals(users.size(), allSpecial.size());
        assertTrue(users.containsAll(allSpecial));
    }

    @Test
    void existsByLogin() {
        String name = "EXISTS";
        User user = new User();
        user.setName(name);
        User saved = userRepository.save(user);
        assertTrue(userRepository.existsByName(name));
        userRepository.deleteById(saved.getId());
        assertFalse(userRepository.existsByName(name));
        assertFalse(userRepository.existsByName("__NOT_EXISTING__"));
    }
}
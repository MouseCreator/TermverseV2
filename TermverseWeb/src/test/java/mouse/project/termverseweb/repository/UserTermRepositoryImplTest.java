package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.model.Term;
import mouse.project.termverseweb.model.User;
import mouse.project.termverseweb.model.UserTerm;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserTermRepositoryImplTest {
    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private TermRepository termRepository;
    @InitBeforeEach
    private UserTermRepository repository;
    @InitBeforeEach
    private Insertions insertions;
    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    private List<UserTerm> insertData(String base, int count) {
        List<User> users = insertions.generateUsers(base, 1);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user = savedUsers.get(0);

        List<Term> terms = insertions.generateTerms(base, count);
        List<Term> savedTerms = insertions.saveAll(termRepository, terms);

        return insertions.bindProgress(repository, user, savedTerms);
    }
    @Test
    void save() {
        List<UserTerm> userTerms = insertData("saved", 4);
        assertEquals(4, userTerms.size());
    }
    @Test
    void findAll() {
        List<UserTerm> userTerms = insertData("find-all", 2);
        List<UserTerm> all = repository.findAll();
        MTest.noDuplicates(all);
        MTest.containsAll(all, userTerms);
    }


    @Test
    void deleteByUserAndTerms() {
        List<UserTerm> userTerms = insertData("delete", 4);
        List<Term> terms = userTerms.stream().map(UserTerm::getTerm).toList();
        List<Long> ids = terms.stream().map(Term::getId).toList();

        Long userId = userTerms.get(0).getUser().getId();

        List<Long> Id1 = ids.subList(2, 4);
        List<UserTerm> expectedRemaining = userTerms.subList(0, 2);
        repository.deleteByUserAndTerms(userId, Id1);
        List<UserTerm> after = repository.findByUserAndTerms(userId, ids);
        MTest.compareUnordered(expectedRemaining, after);
    }

    @Test
    void findByUserAndTerms() {
        List<UserTerm> userTerms = insertData("find-all", 3);
        List<Term> terms = userTerms.stream().map(UserTerm::getTerm).toList();
        List<Long> ids = terms.stream().map(Term::getId).toList();

        Long userId = userTerms.getFirst().getUser().getId();
        List<UserTerm> byIds = repository.findByUserAndTerms(userId, ids);
        MTest.compareUnordered(userTerms, byIds);

        Long Id1 = ids.getFirst();
        UserTerm first = userTerms.getFirst();
        List<UserTerm> firstOnly = repository.findByUserAndTerms(userId, List.of(Id1));
        MTest.compareUnordered(List.of(first), firstOnly);

    }
}
package mouse.project.termverseweb.repository;

import mouse.project.lib.data.page.Page;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageDescriptionImpl;
import mouse.project.lib.data.page.PageFactory;
import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.lib.testutil.MTest;
import mouse.project.termverseweb.defines.UserStudySetRelation;
import mouse.project.termverseweb.lib.test.deletion.SoftDeletionTest;
import mouse.project.termverseweb.model.*;
import mouse.project.termverseweb.mouselib.TestContainer;
import mouse.project.termverseweb.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StudySetRepositoryImplTest {

    @InitBeforeEach
    private StudySetRepository repository;
    @InitBeforeEach
    private TermRepository termRepository;
    @InitBeforeEach
    private UserRepository userRepository;
    @InitBeforeEach
    private UserStudySetRepository userStudySetRepository;
    @InitBeforeEach
    private StudySetTermRepository setTermRepository;
    @InitBeforeEach
    private SoftDeletionTest soft;
    @InitBeforeEach
    private Insertions insertions;
    @InitBeforeEach
    private PageFactory pageFactory;

    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
    }
    @BeforeAll
    static void beforeAll() {
        TestContainer.initializeData();
    }

    private List<StudySet> insertData(String base, int count) {
        List<StudySet> sets = insertions.generateStudySets(base, count);
        return insertions.saveAll(repository, sets);
    }
    private StudySet insertData(String base) {
        List<StudySet> sets = insertions.generateStudySets(base, 1);
        return insertions.saveAll(repository, sets).getFirst();
    }
    @Test
    void save() {
        List<StudySet> sets = insertData("Save", 4);
        assertEquals(4, sets.size());
    }
    @Test
    void findAll() {
        List<StudySet> sets = insertData("Study sets All", 3);
        List<StudySet> all = repository.findAll();
        assertTrue(all.containsAll(sets));
    }

    @Test
    void findAllIncludeDeleted() {
        StudySet set = insertData("FAID");
        assertTrue(repository.findAllIncludeDeleted().contains(set));
        soft().remove(set).
                byIds().validatePresentIn(() -> repository.findAllIncludeDeleted());
    }

    @Test
    void findAllByNameIgnoreCase() {
        String specialName = "__SPECIAL_NAME__";
        String lower = specialName.toLowerCase();
        List<StudySet> sets = insertData(specialName, 2);

        List<StudySet> allByNameIgnoreCase = repository.findAllByNameIgnoreCase(lower);
        assertEquals(sets.size(), allByNameIgnoreCase.size());
        assertTrue(allByNameIgnoreCase.containsAll(sets));
    }

    @Test
    void findAllByCreatedDateRange() {
        List<StudySet> sets = insertions.generateStudySets("Time", 2);
        StudySet set1 = sets.get(0);
        set1.setCreatedAt(DateUtils.fromString("2010-01-10 10:00:00"));
        StudySet set2 = sets.get(1);
        set2.setCreatedAt(DateUtils.fromString("2010-01-12 10:00:00"));
        insertions.saveAll(repository, sets);

        LocalDateTime before = DateUtils.fromString("2010-01-08 10:00:00");
        LocalDateTime between = DateUtils.fromString("2010-01-11 10:00:00");
        LocalDateTime after = DateUtils.fromString("2010-01-12 10:00:00");
        List<StudySet> all = repository.findAllByCreatedDateRange(before, after);
        assertEquals(2, all.size());

        List<StudySet> first = repository.findAllByCreatedDateRange(before, between);
        assertEquals(1, first.size());
        assertEquals(set1, first.get(0));

        List<StudySet> second = repository.findAllByCreatedDateRange(between, after);
        assertEquals(1, second.size());
        assertEquals(set2, second.get(0));
    }

    @Test
    void deleteById() {
        StudySet studySet = insertData("Delete-by-id");
        Long id = studySet.getId();

        soft().remove(studySet)
                .byIds().assertTrue(()->repository.findById(id).isEmpty())
                .byIds().assertTrue(()->repository.findByIdIncludeDeleted(id).isPresent());
    }

    private SoftDeletionTest.BeforeSoftDeletion<StudySet, Long> soft() {
        return soft.using(repository::deleteById, StudySet::getId);
    }

    @Test
    void restoreById() {
        StudySet studySet = insertData("Restore-by-id");
        Long id = studySet.getId();

        soft().remove(studySet)
                .byIds().assertTrue(()->repository.findById(id).isEmpty())
                .restoreWith(repository::restoreById)
                .byIds().assertTrue(()->repository.findById(id).isPresent());
    }



    @Test
    void findById() {
        StudySet studySet = insertData("Find-by-id");
        Long id = studySet.getId();
        Optional<StudySet> byId = repository.findById(id);
        assertTrue(byId.isPresent());
        assertEquals(studySet, byId.get());

        Optional<StudySet> notExist = repository.findById(Long.MAX_VALUE);
        assertTrue(notExist.isEmpty());
    }

    @Test
    void findByIdIncludeDeleted() {
        StudySet studySet = insertData("Find-by-id-deleted");
        Long id = studySet.getId();
        repository.deleteById(id);
        Optional<StudySet> byId = repository.findByIdIncludeDeleted(id);
        assertTrue(byId.isPresent());
        Optional<StudySet> notExist = repository.findByIdIncludeDeleted(Long.MAX_VALUE);
        assertTrue(notExist.isEmpty());
    }

    @Test
    void findAllByUserId() {
        String base = "with-user";
        StudySet studySet = insertData(base);
        User user = insertUserAndAssignToSet(base, studySet);
        Long userId = user.getId();
        List<StudySet> allByUserId = repository.findAllByUserId(userId);
        MTest.compareUnordered(List.of(studySet), allByUserId);

        repository.deleteById(studySet.getId());
        assertTrue(repository.findAllByUserId(userId).isEmpty());
    }

    private User insertUserAndAssignToSet(String base, StudySet studySet) {
        List<User> users = insertions.generateUsers(base, 1);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user = savedUsers.get(0);
        userStudySetRepository.save(new UserStudySet(user, studySet, UserStudySetRelation.OWNER));
        return user;
    }
    private User insertUserAndAssignToSet(String base, List<StudySet> studySets) {
        List<User> users = insertions.generateUsers(base, 1);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user = savedUsers.get(0);
        studySets.forEach(s -> userStudySetRepository.save(new UserStudySet(user, s, UserStudySetRelation.OWNER)));
        return user;
    }

    @Test
    void findAllByUserIdPages() {
        String base = "pageable";
        int size = 2;
        List<StudySet> studySets = insertData(base, size * 2);
        User user = insertUserAndAssignToSet(base, studySets);
        Long userId = user.getId();


        PageDescription page0Description = pageFactory.pageDescription(0, size);
        Page<StudySet> page0 = repository.findAllByUserId(userId, page0Description);
        List<StudySet> page0Elements = studySets.subList(0, 2);
        MTest.compareUnordered(page0.getElements(), page0Elements);

        PageDescription page1Description = pageFactory.pageDescription(1, size);
        Page<StudySet> page1 = repository.findAllByUserId(userId, page1Description);
        List<StudySet> page1Elements = studySets.subList(2, 4);
        MTest.compareUnordered(page1.getElements(), page1Elements);

        PageDescription page2Description = pageFactory.pageDescription(2, size);
        assertTrue(repository.findAllByUserId(userId, page2Description).isEmpty());
    }

    @Test
    void findAllByIdWithTerms() {
        StudySet studySet = insertData("with-terms");
        List<Term> terms = insertTerms(studySet, "Terms1", 3);
        Long id = studySet.getId();
        Optional<StudySet> allByIdWithTerms = repository.findAllByIdWithTerms(id);
        assertTrue(allByIdWithTerms.isPresent());
        StudySet studySetFromDB = allByIdWithTerms.get();
        assertNotNull(studySetFromDB.getTerms());
        MTest.compareUnordered(terms, studySetFromDB.getTerms());

        termRepository.deleteById(terms.get(0).getId());
        List<Term> afterDeletionExp = terms.subList(1, terms.size());
        Optional<StudySet> setUpdated = repository.findAllByIdWithTerms(id);
        assertTrue(setUpdated.isPresent());
        List<Term> afterDeletionAct = setUpdated.get().getTerms();
        MTest.compareUnordered(afterDeletionExp, afterDeletionAct);
    }

    private List<Term> insertTerms(StudySet studySet, String termsBaseName, int count) {
        List<Term> terms = insertions.generateTerms(termsBaseName, count);
        List<Term> savedTerms = insertions.saveAll(termRepository, terms);
        insertions.bindSetTerms(setTermRepository, studySet, savedTerms);
        return savedTerms;
    }

    @Test
    void getTermCount() {
        int count = 5;
        StudySet studySet = insertData("term-count");
        List<Term> terms = insertTerms(studySet, "Terms2", count);
        Long id = studySet.getId();
        assertEquals(count, terms.size());
        Integer termCount = repository.getTermCount(id);
        assertEquals(count, termCount);

        Term term = terms.get(0);
        termRepository.deleteById(term.getId());
        Integer afterDeletion = repository.getTermCount(id);
        assertEquals(count-1, afterDeletion);
    }

    @Test
    void findByIdWithSize() {
        int size = 2;
        StudySet studySet = insertData("with-size");
        List<Term> terms = insertTerms(studySet, "Terms2", size);
        assertEquals(size, terms.size());

        Optional<SizedStudySet> byIdWithSize = repository.findByIdWithSize(studySet.getId());
        assertTrue(byIdWithSize.isPresent());
        SizedStudySet sizedStudySet = byIdWithSize.get();
        assertEquals(size, sizedStudySet.size());
        assertEquals(studySet, sizedStudySet.studySet());
    }
    @Test
    void testAllByNameAndType() {
        List<StudySet> studySet = insertData("by-name-and-type", 2);
        StudySet set1 = studySet.get(0);
        StudySet set2 = studySet.get(1);
        List<User> users = insertions.generateUsers("Bnat", 2);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user1 = savedUsers.get(0);
        User user2 = savedUsers.get(1);
        UserStudySet u11 = userStudySetRepository.save(new UserStudySet(user1, set1, UserStudySetRelation.OWNER));
        UserStudySet u22 = userStudySetRepository.save(new UserStudySet(user2, set2, UserStudySetRelation.OWNER));
        UserStudySet u12 = userStudySetRepository.save(new UserStudySet(user1, set2, UserStudySetRelation.VIEWER));
        Page<UserStudySet> allByNameAndType = repository.findAllByNameAndType("by-name-and-type", UserStudySetRelation.OWNER, new PageDescriptionImpl(0, 3), "");
        List<UserStudySet> elements = allByNameAndType.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(u11));
        assertTrue(elements.contains(u22));
        assertFalse(elements.contains(u12));
    }

    @Test
    void testAllByNameAndUser() {
        List<StudySet> studySet = insertData("by-name-and-user", 2);
        StudySet set1 = studySet.get(0);
        StudySet set2 = studySet.get(1);
        List<User> users = insertions.generateUsers("Bnau", 2);
        List<User> savedUsers = insertions.saveAll(userRepository, users);
        User user1 = savedUsers.get(0);
        User user2 = savedUsers.get(1);
        UserStudySet u21 = userStudySetRepository.save(new UserStudySet(user2, set1, UserStudySetRelation.OWNER));
        UserStudySet u22 = userStudySetRepository.save(new UserStudySet(user2, set2, UserStudySetRelation.OWNER));
        UserStudySet u11 = userStudySetRepository.save(new UserStudySet(user1, set1, UserStudySetRelation.VIEWER));
        Page<UserStudySet> allByNameAndType = repository.
                findAllByNameAndUser("by-name-and-user", user1.getId(),
                UserStudySetRelation.OWNER, new PageDescriptionImpl(0, 3), "");
        List<UserStudySet> elements = allByNameAndType.getElements();
        assertEquals(1, elements.size(), "Unexpected elements: " + elements);
        assertTrue(elements.contains(u21));
        assertFalse(elements.contains(u22));
        assertFalse(elements.contains(u11));
    }
}
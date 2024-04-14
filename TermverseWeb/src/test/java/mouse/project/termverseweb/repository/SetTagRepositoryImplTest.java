package mouse.project.termverseweb.repository;

import mouse.project.lib.tests.annotation.InitBeforeEach;
import mouse.project.termverseweb.mouselib.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetTagRepositoryImplTest {
    @InitBeforeEach
    private SetTagRepository setTagRepository;
    @BeforeEach
    void setUp() {
        TestContainer.setUp(this);
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
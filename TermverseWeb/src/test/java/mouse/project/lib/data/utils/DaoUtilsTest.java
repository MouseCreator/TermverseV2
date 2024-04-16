package mouse.project.lib.data.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DaoUtilsTest {
    private DaoUtils daoUtils;

    @BeforeEach
    void setUp() {
        daoUtils = new DaoUtils();
    }

    @Test
    void idsList() {
        String empty = daoUtils.idsList(List.of());
        assertEquals("()", empty);

        String one = daoUtils.idsList(List.of(5L));
        assertEquals("(5)", one);

        String three = daoUtils.idsList(List.of(3,2,1));
        assertEquals("(3,2,1)", three);
    }

    @Test
    void qMarksList() {
        String empty = daoUtils.qMarksList(List.of());
        assertEquals("()", empty);

        String one = daoUtils.qMarksList(List.of(5L));
        assertEquals("(?)", one);

        String three = daoUtils.qMarksList(List.of(3,2,1));
        assertEquals("(?,?,?)", three);
    }
}
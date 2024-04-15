package mouse.project.lib.testutil;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
public class MTest {
    public static <T> void compareUnordered(Collection<T> c1, Collection<T> c2) {
        if (c1 == c2) {
            return;
        }
        assertNotNull(c1, "Collection 1 is null");
        assertNotNull(c2, "Collection 1 is null");
        assertEquals(c1.size(), c2.size(), "Collection sizes do not match");
        assertEquals(new HashSet<>(c1), new HashSet<>(c2), "Collection elements do not match");
    }
}

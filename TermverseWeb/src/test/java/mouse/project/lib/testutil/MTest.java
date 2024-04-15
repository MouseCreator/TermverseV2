package mouse.project.lib.testutil;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
public class MTest {
    public static <T> void compareUnordered(Collection<T> c1, Collection<T> c2) {
        assertFalse(c1 == null && c2 != null, "Collection 1 is null");
        assertFalse(c1 != null && c2 == null, "Collection 1 is null");
        if (c1 == null) {
            return;
        }
        assertEquals(c1.size(), c2.size(), "Collections size does not match");
        assertEquals(new HashSet<>(c1), new HashSet<>(c2));
    }
}

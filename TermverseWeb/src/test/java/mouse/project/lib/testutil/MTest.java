package mouse.project.lib.testutil;

import mouse.project.termverseweb.model.UserTerm;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    public static <T> void containsAll(List<T> superCollection, List<T> subs) {
        if (superCollection.containsAll(subs)) {
            return;
        }
        List<T> modified = new ArrayList<>(subs);
        modified.removeAll(superCollection);
        fail("Collection " + superCollection + " is missing " + modified);
    }

    public static <T> List<T> distinct(List<T> all) {
        return all.stream().distinct().toList();
    }
}

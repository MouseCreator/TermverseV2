package mouse.project.lib.data.page;

import mouse.project.lib.data.sort.SortFactory;
import mouse.project.lib.data.sort.SortFactoryImpl;
import mouse.project.lib.data.sort.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageFactoryImplTest {

    private PageFactory pageFactory;
    private SortFactory sortFactory;

    @BeforeEach
    void setUp() {
        pageFactory = new PageFactoryImpl();
        sortFactory = new SortFactoryImpl();
    }
    private List<Integer> generateIntegers(int number) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(i);
        }
        return list;
    }

    private List<Integer> generateUnsorted(int number) {
        List<Integer> integers = generateIntegers(number);
        Collections.shuffle(integers);
        return integers;
    }
    @Test
    void pageOf() {
        List<Integer> integers = generateIntegers(20);
        PageDescription tooSmall = pageFactory.pageDescription(2, 4);
        assertThrows(IllegalArgumentException.class, () -> pageFactory.pageOf(integers, tooSmall));

        PageDescription exactly = pageFactory.pageDescription(0, 20);
        Page<Integer> integerPage = pageFactory.pageOf(integers, exactly);
        assertEquals(exactly, integerPage.description());
        assertEquals(integers, integerPage.getElements());

        PageDescription large = pageFactory.pageDescription(1, 40);
        Page<Integer> otherPage = pageFactory.pageOf(integers, large);
        assertEquals(large, otherPage.description());
        assertEquals(integers, otherPage.getElements());
    }

    @Test
    void testImmutable() {
        List<Integer> integers = generateIntegers(5);

        PageDescription pd = pageFactory.pageDescription(0, 5);
        Page<Integer> integerPage = pageFactory.pageOf(integers, pd);

        integers.set(1, 40);
        assertEquals(1, integerPage.getElements().get(1));
    }

    @Test
    void pageDescription() {
        PageDescription pageDescription = pageFactory.pageDescription(30, 40);

        assertEquals(30, pageDescription.number());
        assertEquals(40, pageDescription.size());

        assertThrows(IllegalArgumentException.class, () -> pageFactory.pageDescription(-1, 3));
        assertThrows(IllegalArgumentException.class, () -> pageFactory.pageDescription(3, -1));

    }

    @Test
    void pageOfSort() {
        List<Integer> unsorted = generateUnsorted(10);
        List<Integer> sorted = generateIntegers(10);
        SortOrder<Integer> sortOrder = sortFactory.createSortOrder(Integer::compare);
        PageDescription pageDescription = pageFactory.pageDescription(0, 10);
        Page<Integer> integerPage = pageFactory.pageOf(unsorted, pageDescription, sortOrder);
        assertEquals(sorted, integerPage.getElements());
    }

    @Test
    void applyPageDescription() {
        List<Integer> integers = generateIntegers(15);

        PageDescription page0Desc = pageFactory.pageDescription(0, 10);
        Page<Integer> page0 = pageFactory.applyPageDescription(integers, page0Desc);
        assertEquals(10, page0.getElements().size());
        assertEquals(elements(0, 9), page0.getElements());

        PageDescription page1Desc = pageFactory.pageDescription(1, 10);
        Page<Integer> page1 = pageFactory.applyPageDescription(integers, page1Desc);
        assertEquals(5, page1.getElements().size());
        assertEquals(elements(10, 14), page1.getElements());

        PageDescription page2Desc = pageFactory.pageDescription(2, 10);
        Page<Integer> page2 = pageFactory.applyPageDescription(integers, page2Desc);
        assertTrue(page2.getElements().isEmpty());
    }

    private List<Integer> elements(int fromInc, int toInc) {
        List<Integer> integers = new ArrayList<>();
        for (int i = fromInc; i <= toInc; i++) {
            integers.add(i);
        }
        return integers;
    }

    @Test
    void applyPageDescriptionSort() {
        List<Integer> integers = generateUnsorted(15);
        SortOrder<Integer> sortOrder = sortFactory.createSortOrder(Integer::compare);

        PageDescription page0Desc = pageFactory.pageDescription(0, 10);
        Page<Integer> page0 = pageFactory.applyPageDescription(integers, page0Desc, sortOrder);
        assertEquals(10, page0.getElements().size());
        assertEquals(elements(0, 9), page0.getElements());

        PageDescription page1Desc = pageFactory.pageDescription(1, 10);
        Page<Integer> page1 = pageFactory.applyPageDescription(integers, page1Desc, sortOrder);
        assertEquals(5, page1.getElements().size());
        assertEquals(elements(10, 14), page1.getElements());

        PageDescription page2Desc = pageFactory.pageDescription(2, 10);
        Page<Integer> page2 = pageFactory.applyPageDescription(integers, page2Desc, sortOrder);
        assertTrue(page2.getElements().isEmpty());
    }
}
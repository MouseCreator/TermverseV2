package mouse.project.termverseweb.mapper;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

class MapperTest {
    @Data
    private static class Source {
        private String name;
        public Source(String name) {
            this.name = name;
        }

    }
    @Data
    private static class Destination {
        private String name;

        public Destination(String name) {
            this.name = name;
        }
    }

    private static class NameGenerator {
        private final Random random;

        private NameGenerator() {
            this.random = new Random();
        }

        public String generateName() {
            return "" + random.nextInt();
        }
    }

    private Function<Source, Destination> mapperMethod = null;
    private Function<Source, Destination> nullSafeMapperMethod = null;
    private NameGenerator nameGenerator = null;
    @BeforeEach
    void setUp() {
        mapperMethod = source -> new Destination(source.getName());
        nullSafeMapperMethod = source -> new Destination(source == null ? "Null" : source.getName());
        nameGenerator = new NameGenerator();
    }

    @Test
    void testListMap() {
        List<Source> sourceList = new ArrayList<>();
        int elements = 20;
        for (int i = 0; i < elements; i++) {
            String name = nameGenerator.generateName();
            sourceList.add(new Source(name));
        }
        List<Destination> destinations = Mapper.mapAll(sourceList).to(mapperMethod).get();
        Assertions.assertNotNull(destinations);
        Assertions.assertEquals(sourceList.size(), destinations.size());
        for (int i = 0; i < elements; i++) {
            Assertions.assertEquals(sourceList.get(i).getName(), destinations.get(i).getName(), "Element " + i + " mismatch.");
        }
    }

    @Test
    void testSingleMap() {
        String expectedName = "Test";
        Destination testDestination = Mapper.map(new Source(expectedName)).toAndGet(mapperMethod);
        Assertions.assertEquals(expectedName, testDestination.getName());
    }

    @Test
    void transform() {
        String expectedName = "Test";
        Destination destination = Mapper.transform(new Source(expectedName), mapperMethod);
        Assertions.assertEquals(expectedName, destination.getName());
    }

    @Test
    void testNullConstructs() {
        Assertions.assertThrows(NullPointerException.class, () -> Mapper.map(null));
        Assertions.assertThrows(NullPointerException.class, () -> Mapper.mapAll(null));
        Assertions.assertThrows(NullPointerException.class, () -> Mapper.transform(null, mapperMethod));
    }

    @Test
    void testNullSafe() {
        List<Source> sourceList = new ArrayList<>();
        sourceList.add(new Source("A"));
        sourceList.add(new Source("B"));
        sourceList.add(null);
        sourceList.add(null);
        sourceList.add(null);
        sourceList.add(new Source("C"));

        Assertions.assertThrows(NullPointerException.class, () -> Mapper.mapAll(sourceList).to(mapperMethod));

        List<Destination> destinations = Mapper.mapAll(sourceList).toAndGet(nullSafeMapperMethod);
        Assertions.assertEquals(sourceList.size(), destinations.size());
        Assertions.assertEquals(
                sourceList.stream().filter(Objects::isNull).count(),
                destinations.stream().filter(t -> t.getName().equals("Null")).count()
        );
    }
}
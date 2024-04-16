package mouse.project.lib.ioc.injector.card.scan;

import lombok.RequiredArgsConstructor;
import mouse.project.lib.exception.CardException;
import mouse.project.lib.exception.TypeException;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.injector.card.definition.*;
import mouse.project.lib.ioc.injector.sources.annotation.Construct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefinedCardScannerTest {

    private CardScanner scan;
    private ScanTestHelper helper;
    private interface InterfaceA {

    }
    private static abstract class AbstractA {
    }
    private class NotStaticA {
    }
    @BeforeEach
    void setUp() {
        scan = new DefinedCardScanner();
        helper = new ScanTestHelper();
    }

    @Test
    void scanInvalid() {
        assertThrows(TypeException.class, () -> scan.scan(InterfaceA.class));
        assertThrows(TypeException.class, () -> scan.scan(AbstractA.class));
        assertThrows(TypeException.class, () -> scan.scan(NotStaticA.class));
    }
    private static class EmptyClass {
        @Construct(key = "1")
        public EmptyClass() {
        }
    }
    @Test
    void testEmpty() {
        CardDefinition<EmptyClass> cardDefinition = scan.scan(EmptyClass.class);
        DefinedCardImpl<EmptyClass> definedCard = (DefinedCardImpl<EmptyClass>) cardDefinition;
        assertNotNull(definedCard.getConstructor());

        Constructor<EmptyClass> constructor = definedCard.getConstructor().getConstructor();
        Construct annotation = constructor.getAnnotation(Construct.class);
        assertNotNull(annotation);
        assertEquals("1", annotation.key());

        Constructor<EmptyClass> constructor1 = helper.getConstructor(EmptyClass.class, "1");
        assertEquals(constructor1, constructor);
    }

    private static class MultipleConstructors {
        @Auto
        public MultipleConstructors() {
        }
        @Auto
        public MultipleConstructors(String s) {}

    }
    @Test
    void testMultipleConstructors() {
        assertThrows(CardException.class, () -> scan.scan(MultipleConstructors.class));
    }
    private static class Pair {
        private String str;
        private Integer i;
        @Auto
        public Pair() {
            str = "DEF";
            i = 0;
        }
        public Pair(String s, int i) {
            this.str = s;
            this.i = i;
        }
        @Auto
        public void setStr(String str) {
            this.str = str;
        }

        @Auto
        public void setI(Integer i) {
            this.i = i;
        }
    }

    @Test
    void testPair() {
        CardDefinition<Pair> cardDefinition = scan.scan(Pair.class);
        DefinedCardImpl<Pair> definedCard = (DefinedCardImpl<Pair>) cardDefinition;
        ConstructorDefinition<Pair> constructor = definedCard.getConstructor();
        assertNotNull(constructor);
        assertEquals(0, constructor.getParameters().size());

        List<SetterDefinition> setters = definedCard.getSetters();
        assertEquals(2, setters.size());
        assertEquals(0, definedCard.getFields().size());
    }

    private static class WithFields {
        @Auto
        public WithFields(String d) {
        }
        @Auto
        private Integer bit;
    }

    @Test
    void testWithFields() {
        CardDefinition<WithFields> cardDefinition = scan.scan(WithFields.class);
        DefinedCardImpl<WithFields> definedCard = (DefinedCardImpl<WithFields>) cardDefinition;
        ConstructorDefinition<WithFields> constructor = definedCard.getConstructor();
        assertNotNull(constructor);
        assertEquals(1, constructor.getParameters().size());
        assertEquals(1, definedCard.getFields().size());
        assertEquals(0, definedCard.getSetters().size());
    }

    private static class WithoutDefaultConstructor {
        public WithoutDefaultConstructor(Long id) {

        }

        public WithoutDefaultConstructor() {

        }
    }

    @Test
    void testWithoutDefaultConstructor() {
        assertThrows(CardException.class, () -> scan.scan(WithoutDefaultConstructor.class));
    }
    @RequiredArgsConstructor
    private static class WithRequired {
        private final int id;
    }

    @Test
    void testRequired() {
        DefinedCard<WithRequired> scan1 = scan.scan(WithRequired.class);
        Constructor<WithRequired> constructor = scan1.getConstructor().getConstructor();
        assertEquals(1, constructor.getParameters().length);
    }

    private static abstract class Parent {
        @Auto
        protected String t;
        private Integer v;
    }
    private static class Child extends Parent {
    }

    @Test
    void testInheritance() {
        CardDefinition<Child> cardDefinition = scan.scan(Child.class);
        DefinedCardImpl<Child> definedCard = (DefinedCardImpl<Child>) cardDefinition;
        ConstructorDefinition<Child> constructor = definedCard.getConstructor();
        assertEquals(0, constructor.getParameters().size());
        assertEquals(1, definedCard.getFields().size());
    }

}
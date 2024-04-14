package mouse.project.lib.ioc.injector.card.factory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mouse.project.lib.exception.CycleDependencyException;
import mouse.project.lib.exception.MissingAnnotationException;
import mouse.project.lib.exception.NoCardDefinitionException;
import mouse.project.lib.ioc.annotation.*;
import mouse.project.lib.ioc.injector.card.container.CardContainer;
import mouse.project.lib.ioc.injector.card.container.CardContainerImpl;
import mouse.project.lib.ioc.injector.card.container.Implementation;
import mouse.project.lib.ioc.injector.card.definition.CardDefinition;
import mouse.project.lib.ioc.injector.card.scan.CardScanner;
import mouse.project.lib.ioc.injector.card.scan.DefinedCardScanner;
import mouse.project.lib.ioc.injector.map.DefinedMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CardFactoryImplTest {

    private CardFactoryImpl cardFactory;
    private CardDefinitions cardDefinitions;
    public CardScanner cardScanner;
    @BeforeEach
    void setUp() {
        cardScanner = new DefinedCardScanner();
        cardDefinitions = new CardDefinitionsImpl(new DefinedMapImpl<>());
        CardContainer cardContainer = new CardContainerImpl(new DefinedMapImpl<>());
        cardFactory = new CardFactoryImpl(cardContainer, cardDefinitions);
    }

    private void scanClass(Class<?> clazz) {
        CardDefinition<?> definition = cardScanner.scan(clazz);
        cardDefinitions.add(definition);
    }
    private void scanAll(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            scanClass(clazz);
        }
    }

    private <T> T getUnnamed(Class<T> t) {
        Implementation<T> tImplementation = new Implementation<>(t, null);
        return cardFactory.buildCard(tImplementation);
    }

    private <T> T getNamed(Class<T> t, String name) {
        return cardFactory.buildCard(new Implementation<>(t, name));
    }
    @EqualsAndHashCode
    private static class SampleClass {

    }
    @Test
    void buildCard() {
        scanClass(SampleClass.class);
        SampleClass sampleClass = getUnnamed(SampleClass.class);
        assertNotNull(sampleClass);
        assertEquals(sampleClass, new SampleClass());
    }

    @Data
    private static class Dependency {
        private final String str;

        public Dependency() {
            str = "Text";
        }
    }
    @Data
    private static class Dependant {
        @Auto
        private Dependency dependency;
    }
    @Test
    void buildWithDependency() {
        scanAll(Dependency.class, Dependant.class);
        Dependant dependant = getUnnamed(Dependant.class);
        assertNotNull(dependant);
        assertNotNull(dependant.getDependency());
        assertEquals("Text", dependant.getDependency().getStr());
    }

    @Test
    void buildNotLoadedDependency() {
        scanClass(Dependant.class);
        assertThrows(NoCardDefinitionException.class, () -> getUnnamed(Dependant.class));
    }
    @Data
    private static class ListDependant {
        @Auto
        @Collect(Dependency.class)
        private List<Dependency> dependency;
    }
    @Test
    void buildWithListDependency() {
        scanAll(ListDependant.class, Dependency.class);
        ListDependant dependant = getUnnamed(ListDependant.class);
        assertNotNull(dependant);
        List<Dependency> dependency = dependant.getDependency();
        assertEquals(1, dependency.size());
        assertEquals(ArrayList.class, dependency.getClass());
        assertEquals(new Dependency(), dependency.get(0));
    }

    private static class ListDependantIncorrect {
        @Auto
        private List<Dependency> dependency;
    }
    @Test
    void buildWithListDependencyNoCollectAnnotation() {
        assertThrows(MissingAnnotationException.class, () -> scanAll(ListDependantIncorrect.class, Dependency.class));
    }

    private interface InterfaceExampleA {
        String getString();
    }

    private static class AImplementation1 implements InterfaceExampleA {

        @Override
        public String getString() {
            return "str1";
        }
    }
    @Primary
    private static class AImplementation2 implements InterfaceExampleA {

        @Override
        public String getString() {
            return "str2";
        }
    }
    @Getter
    private static class InterfaceDependantA {
        @Auto
        @Collect(InterfaceExampleA.class)
        private List<InterfaceExampleA> list;
        @Auto
        private InterfaceExampleA single;

        private final AImplementation1 first;

        private final AImplementation2 second;
        @Auto
        public InterfaceDependantA(AImplementation1 first, AImplementation2 second) {
            this.first = first;
            this.second = second;
        }
    }

    @Test
    void buildWithInterfaceDependency() {
        scanClass(InterfaceDependantA.class);
        scanAll(AImplementation1.class, AImplementation2.class);
        InterfaceDependantA dependant = getUnnamed(InterfaceDependantA.class);
        assertNotNull(dependant);
        List<InterfaceExampleA> list = dependant.getList();
        assertEquals(2, list.size());

        InterfaceExampleA interfaceExampleA = dependant.getSingle();
        assertEquals("str2", interfaceExampleA.getString());

        assertEquals("str1", dependant.getFirst().getString());
        assertEquals("str2", dependant.getSecond().getString());
    }
    private static class Cycle1 {
        @Auto
        Cycle2 cycle2;
    }
    private static class Cycle2 {
        @Auto
        Cycle1 cycle1;
    }
    @Test
    void buildWithCycle() {
        scanAll(Cycle1.class, Cycle2.class);
        assertThrows(CycleDependencyException.class, () -> getUnnamed(Cycle1.class));
    }
    @Name(name = "1")
    private static class NamedDependency1 implements InterfaceExampleA{

        @Override
        public String getString() {
            return "Name-1";
        }
    }

    @Name(name = "2")
    @Primary
    private static class NamedDependency2 implements InterfaceExampleA{

        @Override
        public String getString() {
            return "Name-2";
        }
    }
    @Getter
    private static class NameDependant {
        @Auto
        @UseNamed(name = "1")
        private InterfaceExampleA dependency;
    }

    @Test
    void buildNamed() {
        scanAll(NameDependant.class, NamedDependency1.class, NamedDependency2.class);
        InterfaceExampleA named = getNamed(InterfaceExampleA.class, "2");
        assertEquals("Name-2", named.getString());
        NameDependant dependant = getUnnamed(NameDependant.class);
        InterfaceExampleA dependency = dependant.getDependency();
        assertEquals("Name-1", dependency.getString());
    }
    @Getter
    private static class AfterConstructed {
        private InterfaceExampleA dependency = null;
        @After
        public void afterConstructed(InterfaceExampleA interfaceExampleA) {
            this.dependency = interfaceExampleA;
        }
    }
    @Test
    void buildAfterConstructed() {
        scanAll(AfterConstructed.class, AImplementation1.class, AImplementation2.class);
        AfterConstructed afterConstructed = getUnnamed(AfterConstructed.class);
        assertNotNull(afterConstructed);
        InterfaceExampleA dependency = afterConstructed.getDependency();
        assertNotNull(dependency);
        assertEquals("str2", dependency.getString());
    }
}
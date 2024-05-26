package mouse.project.lib.ioc;

import mouse.project.lib.exception.NoCardDefinitionException;
import mouse.project.lib.ioc.base.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IocTest {
    private Inj getMainConfig() {
        return Ioc.getConfiguredInjector(ConfigClass.class);
    }
    private Inj getOtherConfig() {
        return Ioc.getConfiguredInjector(OtherConfig.class);
    }

    @Test
    void testPrimaryAnnotation() {
        Inj inj = getMainConfig();
        ServiceInterface serviceInterface = inj.get(ServiceInterface.class);
        assertNotNull(serviceInterface);
        String string = serviceInterface.getString();
        assertEquals("First", string);
    }

    @Test
    void testOrdering() {
        Inj inj = getMainConfig();
        Collection<ServiceInterface> collection = inj.getAll(ServiceInterface.class);
        assertNotNull(collection);
        List<ServiceInterface> list = new ArrayList<>(collection);
        assertEquals(2, list.size());
        assertEquals("First", list.get(0).getString());
        assertEquals("Second", list.get(1).getString());
    }

    @Test
    void testIncluded() {
        Inj inj = getMainConfig();
        Included included = inj.get(Included.class);
        assertNotNull(included);
        assertTrue(included.getBoolean());
    }

    @Test
    void testRestriction() {
        Inj main = getMainConfig();
        Inj other = getOtherConfig();
        assertDoesNotThrow(() -> main.get(Restricted.class));
        assertThrows(NoCardDefinitionException.class, () -> other.get(Restricted.class));
    }

    @Test
    void testForbidden() {
        Inj main = getMainConfig();
        Inj other = getOtherConfig();
        assertThrows(NoCardDefinitionException.class, () -> main.get(ForbiddenToRelease.class));
        assertDoesNotThrow(() -> other.get(ForbiddenToRelease.class));
    }

    @Test
    void loadCardAnnotated() {
        Inj main = getMainConfig();
        LoadedCard loadedCard = main.get(LoadedCard.class);
        assertNotNull(loadedCard);
        assertEquals("Loaded", loadedCard.getState());
    }
}
package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.exception.ModelScanException;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.lib.data.orm.desc.FieldDescription;
import mouse.project.lib.data.orm.desc.ModelDescription;
import mouse.project.lib.exception.TypeException;
import mouse.project.lib.ioc.injector.card.scan.ScanTestHelper;
import mouse.project.lib.ioc.injector.sources.annotation.Construct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelDescriptorImplTest {

    private ModelDescriptor descriptor;
    private ScanTestHelper helper;
    @BeforeEach
    void setUp() {
        descriptor = new ModelDescriptorImpl();
        helper = new ScanTestHelper();
    }
    private static class ModelA {

        @Construct(key = "A-key")
        public ModelA() {
        }
        @NamedColumn("name-col")
        private String name;
    }
    @Test
    void describe_Simple_Ok() {
        ModelDescription<ModelA> described = descriptor.describe(ModelA.class);
        assertEquals(ModelA.class, described.forClass());


        Constructor<ModelA> expectedConstructor = helper.getConstructor(ModelA.class, "A-key");
        assertNotNull(described.getConstructor());
        assertEquals(expectedConstructor, described.getConstructor());

        List<FieldDescription> fields = described.getFields().getDescriptions();
        assertEquals(1, fields.size());

        FieldDescription fieldDescription = fields.getFirst();

        assertEquals(String.class, fieldDescription.requiredClass());
        Field expectedField = helper.getField(ModelA.class, "name");
        assertEquals(expectedField, fieldDescription.field());

        assertEquals("name-col", fieldDescription.columnName());
    }

    private static class ModelB {

        @NamedColumn
        private Long autonamed;
    }

    @Test
    void describe_Simple_Autonamed() {
        ModelDescription<ModelB> described = descriptor.describe(ModelB.class);
        assertEquals(ModelB.class, described.forClass());

        List<FieldDescription> fields = described.getFields().getDescriptions();
        assertEquals(1, fields.size());

        FieldDescription fieldDescription = fields.getFirst();
        assertEquals(Long.class, fieldDescription.requiredClass());
        assertEquals("autonamed", fieldDescription.columnName());
    }

    private abstract class WrongA {
    }

    @Test
    void describe_Wrong_notProduced() {
        assertThrows(TypeException.class, () -> descriptor.describe(WrongA.class));
    }

    private static class WrongB {
        @NamedColumn
        private List<Long> collection;
    }

    @Test
    void describe_Wrong_collectionField() {
        assertThrows(ModelScanException.class, () -> descriptor.describe(WrongB.class));
    }

    private static class WrongC {
        @NamedColumn
        private String str;

        public WrongC(String str) {
            this.str = str;
        }
    }

    @Test
    void describe_Wrong_noDefaultConstructor() {
        assertThrows(ModelScanException.class, () -> descriptor.describe(WrongC.class));
    }


}
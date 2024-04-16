package mouse.project.lib.data.orm.scan;

import mouse.project.lib.data.orm.annotation.Model;
import mouse.project.lib.data.orm.annotation.NamedColumn;
import mouse.project.lib.data.orm.desc.ModelDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelScannerImplTest {

    private ModelScanner modelScanner;

    @BeforeEach
    void setUp() {
        modelScanner = new ModelScannerImpl(new ModelPackageScanImpl(), new ModelDescriptorImpl());
    }

    @Model
    private static class SampleModel {
        @NamedColumn
        private String str;
    }

    @Test
    void scanAndDescribe() {
        List<ModelDescription<?>> modelDescriptions = modelScanner.scanAndDescribe("mouse.project.lib.data.orm.scan");
        assertEquals(1, modelDescriptions.size());
        assertEquals(SampleModel.class, modelDescriptions.get(0).forClass());
    }
}
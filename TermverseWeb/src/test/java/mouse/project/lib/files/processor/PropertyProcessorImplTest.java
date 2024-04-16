package mouse.project.lib.files.processor;

import mouse.project.lib.files.PropertyMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyProcessorImplTest {
    private PropertyProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new PropertyProcessorImpl();
    }

    @Test
    void process() {
        PropertyMapImpl propertyMap = new PropertyMapImpl();
        List<String> lines = List.of(
                "property1 = hello",
                "property2 = ${M_environment}"
        );
        processor.process(lines, propertyMap);

        assertEquals("hello", propertyMap.getPropertyValue("property1"));
        assertEquals("${M_environment}", propertyMap.getPropertyValue("property2"));
    }
}
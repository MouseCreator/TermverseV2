package mouse.project.lib.files;

import mouse.project.lib.files.manager.FileManager;
import mouse.project.lib.files.processor.PropertyProcessor;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;

import java.util.List;

@Service
public class PropertiesFileReaderImpl implements PropertiesFileReader {

    private final FileManager fileManager;
    private final PropertyProcessor processor;
    @Auto
    public PropertiesFileReaderImpl(FileManager fileManager, PropertyProcessor processor) {
        this.fileManager = fileManager;
        this.processor = processor;
    }

    @Override
    public PropertyMap readFile(String file) {
        List<String> lines = fileManager.readLines(file);
        PropertyMapImpl propertyMap = new PropertyMapImpl();
        processor.process(lines, propertyMap);
        return propertyMap;
    }
}

package mouse.project.lib.files.processor;

import mouse.project.lib.files.MapFiller;

import java.util.Collection;

public interface PropertyProcessor {
    void process(Collection<String> lines, MapFiller map);
}

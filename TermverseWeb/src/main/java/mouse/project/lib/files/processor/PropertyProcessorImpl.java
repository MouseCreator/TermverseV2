package mouse.project.lib.files.processor;

import mouse.project.lib.files.MapFiller;
import mouse.project.lib.files.exception.PropertyException;
import mouse.project.lib.ioc.annotation.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PropertyProcessorImpl implements PropertyProcessor {
    @Override
    public void process(Collection<String> lines, MapFiller map) {
        lines.forEach(
                line -> processLine(line, map)
        );
    }

    private void processLine(String line, MapFiller map) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }
        String[] split = line.split("=", 2);
        if (split.length != 2) {
            throw new PropertyException("Cannot find key and value pair in line: " + line);
        }
        String key = split[0].trim();
        String value = split[1].trim();
        if (isValidPlaceholder(value)) {
            value = fromEnv(value);
        }
        map.put(key, value);
    }

    private String fromEnv(String value) {
        String ev = value.substring(2, value.length()-1);
        String envVar = System.getenv(ev);
        return Optional.ofNullable(envVar).orElse(value);
    }

    private static boolean isValidPlaceholder(String str) {
        if (str == null) {
            return false;
        }

        String regex = "^\\$\\{[a-zA-Z0-9_]+}$";
        return str.matches(regex);
    }
}

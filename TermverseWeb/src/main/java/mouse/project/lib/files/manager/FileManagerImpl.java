package mouse.project.lib.files.manager;

import mouse.project.lib.files.exception.FileException;
import mouse.project.lib.ioc.annotation.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class FileManagerImpl implements FileManager {
    @Override
    public List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new FileException(e);
        }
        return lines;
    }

    @Override
    public String read(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) {
                    content.append("\n");
                } else {
                    first = false;
                }
                content.append(line);
            }
        } catch (IOException e) {
            throw new FileException(e);
        }
        return content.toString();
    }

    @Override
    public void write(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    @Override
    public void append(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
        } catch (IOException e) {
            throw new FileException(e);
        }
    }
}

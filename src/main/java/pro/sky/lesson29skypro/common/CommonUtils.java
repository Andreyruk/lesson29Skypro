package pro.sky.lesson29skypro.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class CommonUtils {

    public String createDirectory(String directory) {
        Path workingDir = Paths.get("").toAbsolutePath();
        Path checkingDir = Paths.get(workingDir+"/"+directory);
        if (!Files.exists(checkingDir)) {
            try {
                Files.createDirectories(checkingDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return checkingDir.toString();
    }

    public <T> boolean writeFile(String sourceDir, int id, T obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(String.format("%s\\%s.json", sourceDir,id));
        try {
            objectMapper.writeValue(file, obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> TreeMap<Integer, T> readObjects(String sourceDir, Class<T> type) {
        List<File> files = readFiles(sourceDir);
        TreeMap<Integer, T> map = new TreeMap<>();
        if (!files.isEmpty()) {
            files.forEach(item -> {
                T obj = readFile(item, type);
                int extIndex = FilenameUtils.indexOfExtension(item.getName());
                map.put(Integer.valueOf(item.getName().substring(0, extIndex)), obj);
            });
        }
        return map;
    }

    public List<File> readFiles(String sourceDir) {
        try {
           return Files.walk(Paths.get(sourceDir))
                   .filter(Files::isRegularFile)
                   .map(Path::toFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T readFile(File file, Class<T> type) {
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return objectFromString(data, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T objectFromString(String jsonString, Class<T> type) {
        T obj;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            obj = objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public boolean deleteFile(String sourceDir) {
        Path path = Paths.get(sourceDir);
        try {
            Files.delete(path);
            return true;
        } catch (NoSuchFileException x) {
            System.err.format("%s: такой файл не существует", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
        return false;
    }
}

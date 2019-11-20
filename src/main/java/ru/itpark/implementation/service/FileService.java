package ru.itpark.implementation.service;

import lombok.val;
import ru.itpark.exceptions.FileAccessException;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileService {
    private final String uploadPath;

    public FileService() {
        try {
            uploadPath = System.getenv(ResourcesPaths.uploadPath);
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    public void readFile(String id, OutputStream os) {
        try {
            val path = Paths.get(uploadPath).resolve(id);
            Files.copy(path, os);
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    public String writeFile(Part part) {
        try {
            val id = UUID.randomUUID().toString();
            part.write(Paths.get(uploadPath).resolve(id).toString());
            return id;
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    public boolean removeFile(String id) {
        try {
            val path = Paths.get(uploadPath).resolve(id);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }
}

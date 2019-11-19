package ru.itpark.implementation.controller;

import lombok.RequiredArgsConstructor;
import ru.itpark.framework.annotation.Component;
import ru.itpark.domain.Auto;
import ru.itpark.implementation.service.AutoService;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
//@RequiredArgsConstructor
public class AutoController {
    private final AutoService service;
    private final String uploadPath;

    public AutoController(AutoService service) throws IOException {
        this.service = service;
        uploadPath = System.getenv(ResourcesPaths.uploadPath);
        Files.createDirectories(Paths.get(uploadPath));
    }

    public List<Auto> getAll() {
        return service.getAll();
    }

    public Auto create(Auto auto) {
        return service.create(auto);
    }

    public void readFile(String id, ServletOutputStream os) throws IOException {
        Path path = Paths.get(uploadPath).resolve(id);
        Files.copy(path, os);
    }

    public String writeFile(Part part) throws IOException {
        String id = UUID.randomUUID().toString();
        part.write(Paths.get(uploadPath).resolve(id).toString());
        return id;
    }

    public List<String> doSearch(String name) {
        System.out.println("do search");
        return Collections.emptyList();
    }
}

package ru.itpark.implementation.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.itpark.exceptions.NotFoundException;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.model.Auto;
import ru.itpark.implementation.repository.AutoRepository;

import javax.servlet.http.Part;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AutoService {
    private final AutoRepository repository;
    private final FileService fileService;

    public List<Auto> getAll() {
        return repository.getAll();
    }

    public Auto getById(long id) {
        return repository.getById(id).orElseThrow(() -> new NotFoundException(String.format("Object with id %d not found", id)));
    }

    public void create(Auto auto, Part part) {
        if (part != null) {
            val image = fileService.writeFile(part);
            auto.setImage(image);
        }
        repository.create(auto);
    }

    public boolean removeById(long id) {
        return fileService.removeFile(repository.removeById(id));
    }

    public List<Auto> search(String text) {
        return repository.search(text);
    }

    public List<Auto> doSearch(String text) {
        return repository.search(text);
    }
}

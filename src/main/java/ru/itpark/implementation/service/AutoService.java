package ru.itpark.implementation.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.model.Auto;
import ru.itpark.implementation.repository.AutoRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AutoService {
    private final AutoRepository repository;

    public List<Auto> getAll() {
        return repository.getAll();
    }

    public Auto create(Auto auto) {
        return repository.create(auto);
    }
}

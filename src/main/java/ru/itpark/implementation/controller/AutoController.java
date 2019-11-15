package ru.itpark.implementation.controller;

import lombok.RequiredArgsConstructor;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.model.Auto;
import ru.itpark.implementation.service.AutoService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AutoController {
    private final AutoService service;

    public List<Auto> getAll() {
        return service.getAll();
    }

    public Auto create(Auto auto) {
        return service.create(auto);
    }

    public List<String> doSearch(String name) {
        System.out.println("do search");
        return Collections.emptyList();
    }
}

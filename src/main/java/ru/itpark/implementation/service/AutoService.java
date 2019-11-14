package ru.itpark.implementation.service;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.repository.AutoRepository;

@Component
public class AutoService {
    private final AutoRepository repository;

    public AutoService(AutoRepository repository) {
        System.out.println("*AutoService created*");
        this.repository = repository;
    }

    public AutoService(int someNumber, AutoRepository repository) {
        System.out.println("*AutoService created by 2nd constructor*");
        this.repository = repository;
    }
}

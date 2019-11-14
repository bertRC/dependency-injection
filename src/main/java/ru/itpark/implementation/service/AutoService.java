package ru.itpark.implementation.service;

import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.repository.AutoRepository;

@Component
public class AutoService {
  private final AutoRepository repository;

  public AutoService(AutoRepository repository) {
    System.out.println("created");
    this.repository = repository;
  }
}

package ru.itpark.framework.container;

import ru.itpark.implementation.repository.AutoRepository;
import ru.itpark.implementation.service.AutoService;

import java.util.HashMap;
import java.util.Map;

public class ContainerStatImpl implements Container {
  @Override
  public Map<Class, Object> init() {
    Map<Class, Object> components = new HashMap<>();
    components.put(AutoRepository.class, new AutoRepository());
    components.put(AutoService.class, new AutoService((AutoRepository) components.get(AutoRepository.class)));
    return components;
  }
}

package ru.itpark.framework.container;

import java.util.Map;

public interface Container {
  Map<Class, Object> init();
}

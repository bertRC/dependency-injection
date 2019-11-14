package ru.itpark.framework.container;

import org.reflections.Reflections;
import ru.itpark.framework.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ContainerProImpl implements Container {

    private Optional<Constructor<?>> getFirstSuitableConstructor(Class<?> type, Set<Class> setOfComponents) {
        return Arrays.stream(type.getConstructors())
                .filter(o -> setOfComponents.containsAll(Arrays.asList(o.getParameterTypes())))
                .findFirst();
    }

    @Override
    public Map<Class, Object> init() {
        final Reflections reflections = new Reflections();
        final Map<Class, Object> components = new HashMap<>();

        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class, true).stream()
                .filter(o -> !o.isAnnotation()).collect(Collectors.toSet());

        while (!types.equals(components.keySet())) {
            Map<Class, Object> currentGeneration = types.stream()
                    .filter(o -> !components.containsKey(o))
                    .filter(o -> getFirstSuitableConstructor(o, components.keySet()).isPresent())
                    .collect(Collectors.toMap(o -> o, o -> {
                        final Constructor<?> constructor = getFirstSuitableConstructor(o, components.keySet()).get();
                        final Object[] params = Arrays.stream(constructor.getParameterTypes())
                                .map(components::get).toArray();
                        try {
                            return constructor.newInstance(params);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }));

            if (currentGeneration.isEmpty()) {
                HashSet<Class<?>> missingComponents = new HashSet<>(types);
                missingComponents.removeAll(components.keySet());
                throw new RuntimeException("Can not create instances of the following components: "
                        + missingComponents);
            }
            components.putAll(currentGeneration);
        }

        if (components.isEmpty()) {
            throw new RuntimeException("There is no components");
        }

        return components;
    }
}

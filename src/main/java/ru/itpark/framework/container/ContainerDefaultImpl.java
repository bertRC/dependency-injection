package ru.itpark.framework.container;

import org.reflections.Reflections;
import ru.itpark.framework.annotation.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class ContainerDefaultImpl implements Container {
    @Override
    public Map<Class, Object> init() {
        final Reflections reflections = new Reflections();
        final Map<Class, Object> components = new HashMap<>();

        final Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class, true).stream()
                .filter(o -> !o.isAnnotation()).collect(Collectors.toSet());

        while (!types.equals(components.keySet())) {
            Map<Class, Object> currentGeneration = types.stream()
                    .filter(o -> !components.containsKey(o))
                    .filter(o -> {
                        final Constructor<?>[] constructors = o.getConstructors();
                        if (constructors.length != 1) {
                            throw new RuntimeException("Ambiguous constructors!");
                        }

                        final Class<?>[] parameterTypes = constructors[0].getParameterTypes();
                        return components.keySet().containsAll(Arrays.asList(parameterTypes));
                    })
                    .collect(Collectors.toMap(o -> o, o -> {
                        final Constructor<?> constructor = o.getConstructors()[0];
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

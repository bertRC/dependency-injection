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
        System.out.println("--- Всё что есть с аннотациями @Component ---");
        System.out.println(types);

        final List<Class<?>> simple = types.stream().filter(o -> {
            if (o.getConstructors().length != 1) {
                throw new RuntimeException("invalid count of constructors: " + o.getName());
            }

            final int parameterCount = o.getConstructors()[0].getParameterCount();
            return parameterCount == 0;
        }).collect(Collectors.toList());

        System.out.println("--- Классы с простым конструктором ---");
        System.out.println(simple);

        // components -> Class vs Object
        // map
        System.out.println("--- Первое поколение ---");
        final Map<Class, Object> firstGeneration = simple.stream()
                .map(o -> {
                    try {
                        return o.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new RuntimeException("invalid count of constructors", e);
                    }
                })
                .collect(Collectors.toMap(o -> o.getClass(), o -> o));
        components.putAll(firstGeneration);
        System.out.println(firstGeneration);

        System.out.println("--- Второе поколение ---");
        Map<Class, Object> secondGeneration = types.stream()
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
                    // AutoService (AutoRepository repository)
                    final Constructor<?> constructor = o.getConstructors()[0];
                    final Object[] params = Arrays.stream(constructor.getParameterTypes())
                            .map(components::get).toArray();
                    try {
                        return constructor.newInstance(params);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }));
        components.putAll(secondGeneration);
        System.out.println(secondGeneration);

        System.out.println("--- Третье поколение ---");
        Map<Class, Object> thirdGeneration = types.stream()
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
                    // AutoService (AutoRepository repository)
                    final Constructor<?> constructor = o.getConstructors()[0];
                    final Object[] params = Arrays.stream(constructor.getParameterTypes())
                            .map(components::get).toArray();
                    try {
                        return constructor.newInstance(params);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }));
        components.putAll(thirdGeneration);
        System.out.println(thirdGeneration);

        System.out.println("--- Четвертое поколение ---");
        Map<Class, Object> fourthGeneration = types.stream()
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
                    // AutoService (AutoRepository repository)
                    final Constructor<?> constructor = o.getConstructors()[0];
                    final Object[] params = Arrays.stream(constructor.getParameterTypes())
                            .map(components::get).toArray();
                    try {
                        return constructor.newInstance(params);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                }));
        components.putAll(fourthGeneration);
        System.out.println(fourthGeneration);

        System.out.println("--- Найденные компоненты ---");
        System.out.println(components);
        return components;
    }

    public static void main(String[] args) {
        final Container container = new ContainerDefaultImpl();
        final Map<Class, Object> components = container.init();
    }
}

package ru.itpark.implementation.repository;

import ru.itpark.framework.annotation.Component;

@Component
public class SomeComponentClass {
    private final NotAComponentClass notAComponentClass;

    public SomeComponentClass(NotAComponentClass notAComponentClass) {
        this.notAComponentClass = notAComponentClass;
    }
}

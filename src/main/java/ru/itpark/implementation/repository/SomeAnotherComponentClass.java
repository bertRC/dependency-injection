package ru.itpark.implementation.repository;

import ru.itpark.framework.annotation.Component;

@Component
public class SomeAnotherComponentClass {
    private final NotAComponentClass notAComponentClass;

    public SomeAnotherComponentClass(NotAComponentClass notAComponentClass) {
        this.notAComponentClass = notAComponentClass;
    }
}

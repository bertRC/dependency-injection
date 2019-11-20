package ru.itpark.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Auto {
    private long id;
    private String name;
    private String description;
    private String image;
}
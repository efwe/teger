package de.fw.teger.model;

import lombok.Data;

@Data
public class Location {
    private Integer lat;
    private Integer lon;
    private final String name;
}

package de.fw.teger.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemperatureMessage {
    private final Location location;
    private final Temperature temperature;
    private final LocalDateTime date;
}

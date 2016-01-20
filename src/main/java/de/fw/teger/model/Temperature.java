package de.fw.teger.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Temperature {
    private TemperatureUnit temperatureUnit = TemperatureUnit.DEGREE_CELCIUS;
    private final BigDecimal value;
}

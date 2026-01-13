package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryTemperature {
    private String country;
    private Double averageTemperature;
}

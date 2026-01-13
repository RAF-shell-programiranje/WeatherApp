package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryStatistics {
    private String country;
    private Long recordCount;
    private Long locationCount;
    private Double averageTemperature;
    private Double averageHumidity;
    private Double latitude;
    private Double longitude;
}

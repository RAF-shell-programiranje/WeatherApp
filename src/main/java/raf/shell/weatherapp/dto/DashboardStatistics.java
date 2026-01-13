package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatistics {
    private Long totalRecords;
    private Long totalLocations;
    private Long totalCountries;
    private Double averageTemperature;
    private Double averageHumidity;
    private Double averagePressure;
    private List<ConditionCount> conditionDistribution;
    private List<CountryTemperature> temperatureByCountry;
    private List<WeatherRecordDTO> hottestLocations;
    private List<WeatherRecordDTO> coldestLocations;
}

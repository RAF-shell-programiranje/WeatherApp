package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.shell.weatherapp.entity.WeatherRecord;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRecordDTO {
    private Long id;
    private LocationDTO location;
    private LocalDateTime lastUpdated;
    private Double temperatureCelsius;
    private Double temperatureFahrenheit;
    private Double feelsLikeCelsius;
    private Double feelsLikeFahrenheit;
    private String conditionText;
    private Integer humidity;
    private Integer cloud;
    private Double visibilityKm;
    private Double uvIndex;
    private Double windMph;
    private Double windKph;
    private Integer windDegree;
    private String windDirection;
    private Double gustMph;
    private Double gustKph;
    private Double pressureMb;
    private Double pressureIn;
    private Double precipMm;
    private Double precipIn;
    private AirQualityDTO airQuality;
    private AstronomyDTO astronomy;

    public static WeatherRecordDTO fromEntity(WeatherRecord record) {
        return WeatherRecordDTO.builder()
                .id(record.getId())
                .location(LocationDTO.fromEntity(record.getLocation()))
                .lastUpdated(record.getLastUpdated())
                .temperatureCelsius(record.getTemperatureCelsius())
                .temperatureFahrenheit(record.getTemperatureFahrenheit())
                .feelsLikeCelsius(record.getFeelsLikeCelsius())
                .feelsLikeFahrenheit(record.getFeelsLikeFahrenheit())
                .conditionText(record.getConditionText())
                .humidity(record.getHumidity())
                .cloud(record.getCloud())
                .visibilityKm(record.getVisibilityKm())
                .uvIndex(record.getUvIndex())
                .windMph(record.getWindMph())
                .windKph(record.getWindKph())
                .windDegree(record.getWindDegree())
                .windDirection(record.getWindDirection())
                .gustMph(record.getGustMph())
                .gustKph(record.getGustKph())
                .pressureMb(record.getPressureMb())
                .pressureIn(record.getPressureIn())
                .precipMm(record.getPrecipMm())
                .precipIn(record.getPrecipIn())
                .airQuality(AirQualityDTO.fromEntity(record.getAirQuality()))
                .astronomy(AstronomyDTO.fromEntity(record.getAstronomy()))
                .build();
    }
}

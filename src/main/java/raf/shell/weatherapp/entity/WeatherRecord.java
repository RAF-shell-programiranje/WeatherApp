package raf.shell.weatherapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_records", indexes = {
    @Index(name = "idx_condition", columnList = "condition_text"),
    @Index(name = "idx_temperature", columnList = "temperature_celsius")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "air_quality_id")
    private AirQuality airQuality;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "astronomy_id")
    private Astronomy astronomy;

    // Timestamps
    @Column(name = "last_updated_epoch")
    private Long lastUpdatedEpoch;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Temperature
    @Column(name = "temperature_celsius")
    private Double temperatureCelsius;

    @Column(name = "temperature_fahrenheit")
    private Double temperatureFahrenheit;

    @Column(name = "feels_like_celsius")
    private Double feelsLikeCelsius;

    @Column(name = "feels_like_fahrenheit")
    private Double feelsLikeFahrenheit;

    // Weather Condition
    @Column(name = "condition_text")
    private String conditionText;

    private Integer humidity;

    private Integer cloud;

    @Column(name = "visibility_km")
    private Double visibilityKm;

    @Column(name = "visibility_miles")
    private Double visibilityMiles;

    @Column(name = "uv_index")
    private Double uvIndex;

    // Wind
    @Column(name = "wind_mph")
    private Double windMph;

    @Column(name = "wind_kph")
    private Double windKph;

    @Column(name = "wind_degree")
    private Integer windDegree;

    @Column(name = "wind_direction")
    private String windDirection;

    @Column(name = "gust_mph")
    private Double gustMph;

    @Column(name = "gust_kph")
    private Double gustKph;

    // Pressure
    @Column(name = "pressure_mb")
    private Double pressureMb;

    @Column(name = "pressure_in")
    private Double pressureIn;

    // Precipitation
    @Column(name = "precip_mm")
    private Double precipMm;

    @Column(name = "precip_in")
    private Double precipIn;
}

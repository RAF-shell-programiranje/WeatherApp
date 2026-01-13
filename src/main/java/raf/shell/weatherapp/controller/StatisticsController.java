package raf.shell.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.shell.weatherapp.dto.ConditionCount;
import raf.shell.weatherapp.dto.CountryStatistics;
import raf.shell.weatherapp.dto.CountryTemperature;
import raf.shell.weatherapp.dto.DashboardStatistics;
import raf.shell.weatherapp.dto.WeatherRecordDTO;
import raf.shell.weatherapp.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatistics> getDashboardStatistics() {
        return ResponseEntity.ok(statisticsService.getDashboardStatistics());
    }

    @GetMapping("/conditions")
    public ResponseEntity<List<ConditionCount>> getConditionDistribution() {
        return ResponseEntity.ok(statisticsService.getConditionDistribution());
    }

    @GetMapping("/temperature-by-country")
    public ResponseEntity<List<CountryTemperature>> getTemperatureByCountry() {
        return ResponseEntity.ok(statisticsService.getTemperatureByCountry());
    }

    @GetMapping("/hottest")
    public ResponseEntity<List<WeatherRecordDTO>> getHottestLocations(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getHottestLocations(limit));
    }

    @GetMapping("/coldest")
    public ResponseEntity<List<WeatherRecordDTO>> getColdestLocations(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getColdestLocations(limit));
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryStatistics>> getCountryStatistics() {
        return ResponseEntity.ok(statisticsService.getCountryStatistics());
    }
}

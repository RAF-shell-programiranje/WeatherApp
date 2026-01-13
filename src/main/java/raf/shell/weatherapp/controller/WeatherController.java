package raf.shell.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.shell.weatherapp.dto.WeatherRecordDTO;
import raf.shell.weatherapp.service.WeatherService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<Page<WeatherRecordDTO>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<WeatherRecordDTO> records = weatherService.getRecordsPaginated(page, size)
                .map(WeatherRecordDTO::fromEntity);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherRecordDTO> getRecordById(@PathVariable Long id) {
        return weatherService.getRecordById(id)
                .map(WeatherRecordDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<WeatherRecordDTO>> getRecordsByCountry(@PathVariable String country) {
        List<WeatherRecordDTO> records = weatherService.getRecordsByCountry(country)
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/condition/{condition}")
    public ResponseEntity<List<WeatherRecordDTO>> getRecordsByCondition(@PathVariable String condition) {
        List<WeatherRecordDTO> records = weatherService.getRecordsByCondition(condition)
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/temperature")
    public ResponseEntity<List<WeatherRecordDTO>> getRecordsByTemperatureRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<WeatherRecordDTO> records = weatherService.getRecordsByTemperatureRange(min, max)
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<WeatherRecordDTO>> getRecordsByLocation(@PathVariable Long locationId) {
        List<WeatherRecordDTO> records = weatherService.getRecordsByLocationId(locationId)
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(records);
    }
}

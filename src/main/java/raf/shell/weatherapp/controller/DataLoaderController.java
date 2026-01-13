package raf.shell.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.shell.weatherapp.service.CsvDataLoaderService;
import raf.shell.weatherapp.service.WeatherService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataLoaderController {

    private final CsvDataLoaderService csvDataLoaderService;
    private final WeatherService weatherService;

    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadCsvData() {
        csvDataLoaderService.loadCsvData();
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Data loaded successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDataLoadStatus() {
        Map<String, Object> status = new HashMap<>();
        long count = weatherService.getTotalRecordsCount();
        status.put("totalRecords", count);
        status.put("dataLoaded", count > 0);
        return ResponseEntity.ok(status);
    }
}

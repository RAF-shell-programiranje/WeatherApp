package raf.shell.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.shell.weatherapp.dto.SystemStatus;
import raf.shell.weatherapp.repository.LocationRepository;
import raf.shell.weatherapp.repository.WeatherRecordRepository;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final WeatherRecordRepository weatherRecordRepository;
    private final LocationRepository locationRepository;
    private final DataSource dataSource;

    @Value("${spring.application.name:WeatherApp}")
    private String applicationName;

    @Value("${spring.datasource.url:unknown}")
    private String datasourceUrl;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());

        // Check database
        try (Connection conn = dataSource.getConnection()) {
            health.put("database", "UP");
        } catch (Exception e) {
            health.put("database", "DOWN");
            health.put("status", "DEGRADED");
        }

        return ResponseEntity.ok(health);
    }

    @GetMapping("/status")
    public ResponseEntity<SystemStatus> status() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;

        // Database status
        String dbStatus = "UP";
        String dbType = "MySQL";
        try (Connection conn = dataSource.getConnection()) {
            dbType = conn.getMetaData().getDatabaseProductName();
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        // Data statistics
        long totalRecords = weatherRecordRepository.count();
        long totalLocations = locationRepository.count();
        long totalCountries = locationRepository.findAllCountries().size();

        SystemStatus status = SystemStatus.builder()
                .status("UP")
                .timestamp(LocalDateTime.now())
                .applicationName(applicationName)
                .javaVersion(System.getProperty("java.version"))
                .uptimeMillis(ManagementFactory.getRuntimeMXBean().getUptime())
                .memory(SystemStatus.MemoryInfo.builder()
                        .totalMemoryMB(totalMemory)
                        .freeMemoryMB(freeMemory)
                        .usedMemoryMB(usedMemory)
                        .maxMemoryMB(maxMemory)
                        .usagePercentage(Math.round((double) usedMemory / maxMemory * 100 * 10) / 10.0)
                        .build())
                .database(SystemStatus.DatabaseInfo.builder()
                        .status(dbStatus)
                        .databaseType(dbType)
                        .connectionUrl(maskPassword(datasourceUrl))
                        .build())
                .data(SystemStatus.DataInfo.builder()
                        .totalWeatherRecords(totalRecords)
                        .totalLocations(totalLocations)
                        .totalCountries(totalCountries)
                        .dataLoaded(totalRecords > 0)
                        .build())
                .build();

        return ResponseEntity.ok(status);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> metrics() {
        Map<String, Object> metrics = new HashMap<>();

        Runtime runtime = Runtime.getRuntime();

        // JVM Metrics
        Map<String, Object> jvm = new HashMap<>();
        jvm.put("availableProcessors", runtime.availableProcessors());
        jvm.put("totalMemoryBytes", runtime.totalMemory());
        jvm.put("freeMemoryBytes", runtime.freeMemory());
        jvm.put("maxMemoryBytes", runtime.maxMemory());
        jvm.put("usedMemoryBytes", runtime.totalMemory() - runtime.freeMemory());
        metrics.put("jvm", jvm);

        // Uptime
        Map<String, Object> uptime = new HashMap<>();
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        uptime.put("milliseconds", uptimeMillis);
        uptime.put("seconds", uptimeMillis / 1000);
        uptime.put("minutes", uptimeMillis / 60000);
        uptime.put("hours", uptimeMillis / 3600000);
        metrics.put("uptime", uptime);

        // Data Metrics
        Map<String, Object> data = new HashMap<>();
        data.put("weatherRecords", weatherRecordRepository.count());
        data.put("locations", locationRepository.count());
        data.put("countries", locationRepository.findAllCountries().size());
        metrics.put("data", data);

        metrics.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(metrics);
    }

    private String maskPassword(String url) {
        if (url == null) return "unknown";
        // Mask password in connection URL if present
        return url.replaceAll("password=[^&]*", "password=****");
    }
}

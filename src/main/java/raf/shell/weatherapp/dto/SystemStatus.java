package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatus {
    private String status;
    private LocalDateTime timestamp;
    private String applicationName;
    private String javaVersion;
    private long uptimeMillis;
    private MemoryInfo memory;
    private DatabaseInfo database;
    private DataInfo data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemoryInfo {
        private long totalMemoryMB;
        private long freeMemoryMB;
        private long usedMemoryMB;
        private long maxMemoryMB;
        private double usagePercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseInfo {
        private String status;
        private String databaseType;
        private String connectionUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataInfo {
        private long totalWeatherRecords;
        private long totalLocations;
        private long totalCountries;
        private boolean dataLoaded;
    }
}

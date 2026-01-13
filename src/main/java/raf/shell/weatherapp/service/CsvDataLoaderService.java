package raf.shell.weatherapp.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.shell.weatherapp.entity.AirQuality;
import raf.shell.weatherapp.entity.Astronomy;
import raf.shell.weatherapp.entity.Location;
import raf.shell.weatherapp.entity.WeatherRecord;
import raf.shell.weatherapp.repository.LocationRepository;
import raf.shell.weatherapp.repository.WeatherRecordRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataLoaderService {

    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;

    @Value("${csv.file.path:data/GlobalWeatherRepository.csv}")
    private String csvFilePath;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional
    public void loadCsvData() {
        log.info("Starting CSV data load from: {}", csvFilePath);

        if (weatherRecordRepository.count() > 0) {
            log.info("Data already loaded. Skipping CSV import.");
            return;
        }

        Map<String, Location> locationCache = new HashMap<>();
        List<WeatherRecord> batchRecords = new ArrayList<>();
        int batchSize = 1000;
        int totalLoaded = 0;

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> allRows = reader.readAll();

            // Skip header row
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                try {
                    WeatherRecord record = parseCsvRow(row, locationCache);
                    if (record != null) {
                        batchRecords.add(record);

                        if (batchRecords.size() >= batchSize) {
                            weatherRecordRepository.saveAll(batchRecords);
                            totalLoaded += batchRecords.size();
                            log.info("Loaded {} records...", totalLoaded);
                            batchRecords.clear();
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error parsing row {}: {}", i, e.getMessage());
                }
            }

            // Save remaining records
            if (!batchRecords.isEmpty()) {
                weatherRecordRepository.saveAll(batchRecords);
                totalLoaded += batchRecords.size();
            }

            log.info("CSV data load complete. Total records: {}", totalLoaded);
        } catch (IOException | CsvException e) {
            log.error("Error loading CSV data", e);
            throw new RuntimeException("Failed to load CSV data", e);
        }
    }

    private WeatherRecord parseCsvRow(String[] row, Map<String, Location> locationCache) {
        if (row.length < 41) {
            return null;
        }

        // Get or create location
        String country = row[0];
        String locationName = row[1];
        String locationKey = country + "|" + locationName;

        Location location = locationCache.get(locationKey);
        if (location == null) {
            location = locationRepository.findByCountryAndLocationName(country, locationName)
                    .orElseGet(() -> {
                        Location newLocation = new Location();
                        newLocation.setCountry(country);
                        newLocation.setLocationName(locationName);
                        newLocation.setLatitude(parseDouble(row[2]));
                        newLocation.setLongitude(parseDouble(row[3]));
                        newLocation.setTimezone(row[4]);
                        return locationRepository.save(newLocation);
                    });
            locationCache.put(locationKey, location);
        }

        // Create air quality
        AirQuality airQuality = new AirQuality();
        airQuality.setCarbonMonoxide(parseDouble(row[27]));
        airQuality.setOzone(parseDouble(row[28]));
        airQuality.setNitrogenDioxide(parseDouble(row[29]));
        airQuality.setSulphurDioxide(parseDouble(row[30]));
        airQuality.setPm25(parseDouble(row[31]));
        airQuality.setPm10(parseDouble(row[32]));
        airQuality.setUsEpaIndex(parseInteger(row[33]));
        airQuality.setGbDefraIndex(parseInteger(row[34]));

        // Create astronomy
        Astronomy astronomy = new Astronomy();
        astronomy.setSunrise(row[35]);
        astronomy.setSunset(row[36]);
        astronomy.setMoonrise(row[37]);
        astronomy.setMoonset(row[38]);
        astronomy.setMoonPhase(row[39]);
        astronomy.setMoonIllumination(parseInteger(row[40]));

        // Create weather record
        WeatherRecord record = new WeatherRecord();
        record.setLocation(location);
        record.setAirQuality(airQuality);
        record.setAstronomy(astronomy);
        record.setLastUpdatedEpoch(parseLong(row[5]));
        record.setLastUpdated(parseDateTime(row[6]));
        record.setTemperatureCelsius(parseDouble(row[7]));
        record.setTemperatureFahrenheit(parseDouble(row[8]));
        record.setConditionText(row[9]);
        record.setWindMph(parseDouble(row[10]));
        record.setWindKph(parseDouble(row[11]));
        record.setWindDegree(parseInteger(row[12]));
        record.setWindDirection(row[13]);
        record.setPressureMb(parseDouble(row[14]));
        record.setPressureIn(parseDouble(row[15]));
        record.setPrecipMm(parseDouble(row[16]));
        record.setPrecipIn(parseDouble(row[17]));
        record.setHumidity(parseInteger(row[18]));
        record.setCloud(parseInteger(row[19]));
        record.setFeelsLikeCelsius(parseDouble(row[20]));
        record.setFeelsLikeFahrenheit(parseDouble(row[21]));
        record.setVisibilityKm(parseDouble(row[22]));
        record.setVisibilityMiles(parseDouble(row[23]));
        record.setUvIndex(parseDouble(row[24]));
        record.setGustMph(parseDouble(row[25]));
        record.setGustKph(parseDouble(row[26]));

        return record;
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}

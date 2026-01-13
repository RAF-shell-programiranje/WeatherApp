package raf.shell.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import raf.shell.weatherapp.dto.*;
import raf.shell.weatherapp.entity.WeatherRecord;
import raf.shell.weatherapp.repository.LocationRepository;
import raf.shell.weatherapp.repository.WeatherRecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final LocationRepository locationRepository;

    public DashboardStatistics getDashboardStatistics() {
        return DashboardStatistics.builder()
                .totalRecords(weatherRecordRepository.count())
                .totalLocations(locationRepository.count())
                .totalCountries(locationRepository.countDistinctCountries())
                .averageTemperature(weatherRecordRepository.findAverageTemperature())
                .averageHumidity(weatherRecordRepository.findAverageHumidity())
                .averagePressure(weatherRecordRepository.findAveragePressure())
                .conditionDistribution(getConditionDistribution())
                .temperatureByCountry(getTemperatureByCountry())
                .hottestLocations(getHottestLocations(10))
                .coldestLocations(getColdestLocations(10))
                .build();
    }

    public List<ConditionCount> getConditionDistribution() {
        return weatherRecordRepository.countByCondition().stream()
                .map(arr -> new ConditionCount((String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    public List<CountryTemperature> getTemperatureByCountry() {
        return weatherRecordRepository.findAverageTemperatureByCountry().stream()
                .limit(20)
                .map(arr -> new CountryTemperature((String) arr[0], (Double) arr[1]))
                .collect(Collectors.toList());
    }

    public List<WeatherRecordDTO> getHottestLocations(int limit) {
        return weatherRecordRepository.findHottestLocations(PageRequest.of(0, limit))
                .getContent()
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<WeatherRecordDTO> getColdestLocations(int limit) {
        return weatherRecordRepository.findColdestLocations(PageRequest.of(0, limit))
                .getContent()
                .stream()
                .map(WeatherRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

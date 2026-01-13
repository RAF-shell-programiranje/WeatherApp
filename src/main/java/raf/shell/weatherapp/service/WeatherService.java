package raf.shell.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import raf.shell.weatherapp.entity.WeatherRecord;
import raf.shell.weatherapp.repository.WeatherRecordRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRecordRepository weatherRecordRepository;

    public List<WeatherRecord> getAllRecords() {
        return weatherRecordRepository.findAll();
    }

    public Page<WeatherRecord> getRecordsPaginated(int page, int size) {
        return weatherRecordRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<WeatherRecord> getRecordById(Long id) {
        return weatherRecordRepository.findById(id);
    }

    public List<WeatherRecord> getRecordsByCountry(String country) {
        return weatherRecordRepository.findByCountry(country);
    }

    public List<WeatherRecord> getRecordsByCondition(String condition) {
        return weatherRecordRepository.findByConditionText(condition);
    }

    public List<WeatherRecord> getRecordsByTemperatureRange(Double min, Double max) {
        return weatherRecordRepository.findByTemperatureRange(min, max);
    }

    public List<WeatherRecord> getRecordsByLocationId(Long locationId) {
        return weatherRecordRepository.findByLocationId(locationId);
    }

    public long getTotalRecordsCount() {
        return weatherRecordRepository.count();
    }
}

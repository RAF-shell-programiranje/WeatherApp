package raf.shell.weatherapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import raf.shell.weatherapp.entity.Location;
import raf.shell.weatherapp.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Page<Location> getLocationsPaginated(int page, int size) {
        return locationRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getLocationsByCountry(String country) {
        return locationRepository.findByCountry(country);
    }

    public List<String> getAllCountries() {
        return locationRepository.findAllCountries();
    }

    public List<Location> searchLocations(String query) {
        return locationRepository.findByLocationNameContainingIgnoreCase(query);
    }
}

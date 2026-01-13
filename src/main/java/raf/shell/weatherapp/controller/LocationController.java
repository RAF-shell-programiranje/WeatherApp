package raf.shell.weatherapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.shell.weatherapp.dto.LocationDTO;
import raf.shell.weatherapp.service.LocationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<Page<LocationDTO>> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<LocationDTO> locations = locationService.getLocationsPaginated(page, size)
                .map(LocationDTO::fromEntity);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(LocationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<LocationDTO>> getLocationsByCountry(@PathVariable String country) {
        List<LocationDTO> locations = locationService.getLocationsByCountry(country)
                .stream()
                .map(LocationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        return ResponseEntity.ok(locationService.getAllCountries());
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> searchLocations(@RequestParam String query) {
        List<LocationDTO> locations = locationService.searchLocations(query)
                .stream()
                .map(LocationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locations);
    }
}

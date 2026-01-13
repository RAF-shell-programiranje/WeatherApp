package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.shell.weatherapp.entity.Location;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Long id;
    private String country;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private String timezone;

    public static LocationDTO fromEntity(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .country(location.getCountry())
                .locationName(location.getLocationName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .timezone(location.getTimezone())
                .build();
    }
}

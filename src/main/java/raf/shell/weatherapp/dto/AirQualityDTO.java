package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.shell.weatherapp.entity.AirQuality;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirQualityDTO {
    private Double carbonMonoxide;
    private Double ozone;
    private Double nitrogenDioxide;
    private Double sulphurDioxide;
    private Double pm25;
    private Double pm10;
    private Integer usEpaIndex;
    private Integer gbDefraIndex;

    public static AirQualityDTO fromEntity(AirQuality airQuality) {
        if (airQuality == null) return null;
        return AirQualityDTO.builder()
                .carbonMonoxide(airQuality.getCarbonMonoxide())
                .ozone(airQuality.getOzone())
                .nitrogenDioxide(airQuality.getNitrogenDioxide())
                .sulphurDioxide(airQuality.getSulphurDioxide())
                .pm25(airQuality.getPm25())
                .pm10(airQuality.getPm10())
                .usEpaIndex(airQuality.getUsEpaIndex())
                .gbDefraIndex(airQuality.getGbDefraIndex())
                .build();
    }
}

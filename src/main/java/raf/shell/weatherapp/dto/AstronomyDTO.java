package raf.shell.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.shell.weatherapp.entity.Astronomy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AstronomyDTO {
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
    private String moonPhase;
    private Integer moonIllumination;

    public static AstronomyDTO fromEntity(Astronomy astronomy) {
        if (astronomy == null) return null;
        return AstronomyDTO.builder()
                .sunrise(astronomy.getSunrise())
                .sunset(astronomy.getSunset())
                .moonrise(astronomy.getMoonrise())
                .moonset(astronomy.getMoonset())
                .moonPhase(astronomy.getMoonPhase())
                .moonIllumination(astronomy.getMoonIllumination())
                .build();
    }
}

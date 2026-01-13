package raf.shell.weatherapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "air_quality")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirQuality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carbon_monoxide")
    private Double carbonMonoxide;

    private Double ozone;

    @Column(name = "nitrogen_dioxide")
    private Double nitrogenDioxide;

    @Column(name = "sulphur_dioxide")
    private Double sulphurDioxide;

    @Column(name = "pm25")
    private Double pm25;

    @Column(name = "pm10")
    private Double pm10;

    @Column(name = "us_epa_index")
    private Integer usEpaIndex;

    @Column(name = "gb_defra_index")
    private Integer gbDefraIndex;
}

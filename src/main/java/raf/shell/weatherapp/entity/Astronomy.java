package raf.shell.weatherapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "astronomy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Astronomy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sunrise;

    private String sunset;

    private String moonrise;

    private String moonset;

    @Column(name = "moon_phase")
    private String moonPhase;

    @Column(name = "moon_illumination")
    private Integer moonIllumination;
}

package raf.shell.weatherapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"country", "location_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    private Double latitude;

    private Double longitude;

    private String timezone;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WeatherRecord> weatherRecords = new ArrayList<>();
}

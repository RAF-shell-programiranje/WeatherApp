package raf.shell.weatherapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.shell.weatherapp.entity.AirQuality;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {
}

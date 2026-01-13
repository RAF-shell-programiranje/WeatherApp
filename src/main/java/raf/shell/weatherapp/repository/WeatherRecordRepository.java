package raf.shell.weatherapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raf.shell.weatherapp.entity.WeatherRecord;

import java.util.List;

@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    List<WeatherRecord> findByLocationId(Long locationId);

    @Query("SELECT w FROM WeatherRecord w WHERE w.location.country = :country")
    List<WeatherRecord> findByCountry(@Param("country") String country);

    List<WeatherRecord> findByConditionText(String conditionText);

    @Query("SELECT w FROM WeatherRecord w WHERE w.temperatureCelsius BETWEEN :min AND :max")
    List<WeatherRecord> findByTemperatureRange(@Param("min") Double min, @Param("max") Double max);

    @Query("SELECT AVG(w.temperatureCelsius) FROM WeatherRecord w")
    Double findAverageTemperature();

    @Query("SELECT AVG(w.humidity) FROM WeatherRecord w")
    Double findAverageHumidity();

    @Query("SELECT AVG(w.pressureMb) FROM WeatherRecord w")
    Double findAveragePressure();

    @Query("SELECT w.conditionText, COUNT(w) FROM WeatherRecord w GROUP BY w.conditionText ORDER BY COUNT(w) DESC")
    List<Object[]> countByCondition();

    @Query("SELECT w.location.country, AVG(w.temperatureCelsius) FROM WeatherRecord w GROUP BY w.location.country ORDER BY AVG(w.temperatureCelsius) DESC")
    List<Object[]> findAverageTemperatureByCountry();

    @Query("SELECT w FROM WeatherRecord w ORDER BY w.temperatureCelsius DESC")
    Page<WeatherRecord> findHottestLocations(Pageable pageable);

    @Query("SELECT w FROM WeatherRecord w ORDER BY w.temperatureCelsius ASC")
    Page<WeatherRecord> findColdestLocations(Pageable pageable);

    @Query("SELECT w FROM WeatherRecord w WHERE w.location.country = :country OR w.location.locationName LIKE %:query%")
    Page<WeatherRecord> searchByCountryOrLocation(@Param("country") String country, @Param("query") String query, Pageable pageable);
}

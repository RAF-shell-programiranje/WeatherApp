package raf.shell.weatherapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.shell.weatherapp.entity.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCountryAndLocationName(String country, String locationName);

    List<Location> findByCountry(String country);

    List<Location> findByCountryContainingIgnoreCase(String country);

    List<Location> findByLocationNameContainingIgnoreCase(String locationName);

    @Query("SELECT DISTINCT l.country FROM Location l ORDER BY l.country")
    List<String> findAllCountries();

    @Query("SELECT COUNT(DISTINCT l.country) FROM Location l")
    Long countDistinctCountries();
}

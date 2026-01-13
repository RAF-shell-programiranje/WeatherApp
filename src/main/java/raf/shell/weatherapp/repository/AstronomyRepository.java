package raf.shell.weatherapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.shell.weatherapp.entity.Astronomy;

@Repository
public interface AstronomyRepository extends JpaRepository<Astronomy, Long> {
}

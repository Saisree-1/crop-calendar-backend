package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.WeatherData;

public interface WeatherDataRepo extends JpaRepository<WeatherData, Long> {
}
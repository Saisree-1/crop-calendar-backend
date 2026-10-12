package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.SoilType;

public interface SoilTypeRepo extends JpaRepository<SoilType, Integer> {
}
package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.CropVariety;

public interface CropVarietyRepo extends JpaRepository<CropVariety, Integer> {
}
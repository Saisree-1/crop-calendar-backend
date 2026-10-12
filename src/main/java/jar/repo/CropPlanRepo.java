package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.CropPlan;

public interface CropPlanRepo extends JpaRepository<CropPlan, Long> {
}
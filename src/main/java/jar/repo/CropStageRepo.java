package jar.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.CropStage;

public interface CropStageRepo extends JpaRepository<CropStage, Integer> {

    Optional<CropStage> findById(Integer stageId);

}
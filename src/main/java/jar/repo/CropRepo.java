package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Crop;

public interface CropRepo extends JpaRepository<Crop, Integer> {

}
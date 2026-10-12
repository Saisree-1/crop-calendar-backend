package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Disease;

public interface DiseaseRepo extends JpaRepository<Disease, Integer> {
}
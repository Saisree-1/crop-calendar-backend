package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Farm;

public interface FarmRepo extends JpaRepository<Farm, Integer> {

}
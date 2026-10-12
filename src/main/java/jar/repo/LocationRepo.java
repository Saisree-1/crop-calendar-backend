package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Location;

public interface LocationRepo extends JpaRepository<Location, Integer> {
}
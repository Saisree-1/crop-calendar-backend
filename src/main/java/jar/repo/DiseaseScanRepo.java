package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.DiseaseScan;

public interface DiseaseScanRepo extends JpaRepository<DiseaseScan, Long> {
}
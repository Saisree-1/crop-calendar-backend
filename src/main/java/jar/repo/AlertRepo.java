package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Alert;

public interface AlertRepo extends JpaRepository<Alert, Long> {
}
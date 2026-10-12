package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.IvrCallLog;

public interface IvrCallLogRepo extends JpaRepository<IvrCallLog, Long> {
}

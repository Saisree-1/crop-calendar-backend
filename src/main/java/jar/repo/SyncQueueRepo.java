package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.SyncQueue;

public interface SyncQueueRepo extends JpaRepository<SyncQueue, Long> {
}
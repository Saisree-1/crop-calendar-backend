package jar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.SyncQueue;
import jar.repo.SyncQueueRepo;

@RestController
@RequestMapping("/sync-queue")
public class SyncQueueController {

    private final SyncQueueRepo repo;

    public SyncQueueController(SyncQueueRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<SyncQueue> getAllSyncRecords() {
        return repo.findAll();
    }
}
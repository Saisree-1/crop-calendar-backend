package jar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.DiseaseScan;
import jar.repo.DiseaseScanRepo;

@RestController
@RequestMapping("/disease-scans")
public class DiseaseScanController {

    private final DiseaseScanRepo repo;

    public DiseaseScanController(DiseaseScanRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<DiseaseScan> getAllScans() {
        return repo.findAll();
    }
}
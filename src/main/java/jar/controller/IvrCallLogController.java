package jar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.IvrCallLog;
import jar.repo.IvrCallLogRepo;

@RestController
@RequestMapping("/ivr-call-logs")
public class IvrCallLogController {

    private final IvrCallLogRepo repo;

    public IvrCallLogController(IvrCallLogRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<IvrCallLog> getAllCallLogs() {
        return repo.findAll();
    }
}
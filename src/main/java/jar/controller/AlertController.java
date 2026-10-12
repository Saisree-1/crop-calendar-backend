package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Alert;
import jar.repo.AlertRepo;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertRepo alertRepo;

    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepo.findAll();
    }
}
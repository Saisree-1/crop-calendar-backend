package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Farm;
import jar.repo.FarmRepo;

@RestController
@RequestMapping("/farms")
public class FarmController {

    @Autowired
    private FarmRepo farmRepo;

    @GetMapping
    public List<Farm> getFarms() {
        return farmRepo.findAll();
    }

    @PostMapping
    public Farm createFarm(@RequestBody Farm farm) {
        return farmRepo.save(farm);
    }
}
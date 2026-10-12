package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.SoilType;
import jar.repo.SoilTypeRepo;

@RestController
@RequestMapping("/soil-types")
public class SoilTypeController {

    @Autowired
    private SoilTypeRepo soilTypeRepo;

    @GetMapping
    public List<SoilType> getSoilTypes() {
        return soilTypeRepo.findAll();
    }
}
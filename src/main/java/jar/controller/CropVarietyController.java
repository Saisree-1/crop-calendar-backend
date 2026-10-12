package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.CropVariety;
import jar.repo.CropVarietyRepo;

@RestController
@RequestMapping("/crop-varieties")
public class CropVarietyController {

    @Autowired
    private CropVarietyRepo cropVarietyRepo;

    @GetMapping
    public List<CropVariety> getCropVarieties() {
        return cropVarietyRepo.findAll();
    }
}
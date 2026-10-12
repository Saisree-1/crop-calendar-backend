package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.CropPlan;
import jar.repo.CropPlanRepo;

@RestController
@RequestMapping("/crop-plans")
public class CropPlanController {

    @Autowired
    private CropPlanRepo cropPlanRepo;

    @GetMapping
    public List<CropPlan> getCropPlans() {
        return cropPlanRepo.findAll();
    }

    @PostMapping
    public CropPlan createCropPlan(@RequestBody CropPlan cropPlan) {
        return cropPlanRepo.save(cropPlan);
    }
}
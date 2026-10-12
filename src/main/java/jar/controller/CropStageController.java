package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.CropStage;
import jar.repo.CropStageRepo;

@RestController
@RequestMapping("/crop-stages")
public class CropStageController {

    @Autowired
    private CropStageRepo cropStageRepo;

    @GetMapping
    public List<CropStage> getCropStages() {
        return cropStageRepo.findAll();
    }
}
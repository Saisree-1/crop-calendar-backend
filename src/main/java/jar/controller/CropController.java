package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Crop;
import jar.repo.CropRepo;

@RestController
@RequestMapping("/crops")
public class CropController {

    @Autowired
    private CropRepo cropRepo;

    @GetMapping
    public List<Crop> getCrops() {
        return cropRepo.findAll();
    }
}
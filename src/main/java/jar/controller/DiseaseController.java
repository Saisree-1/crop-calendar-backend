package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Disease;
import jar.repo.DiseaseRepo;

@RestController
@RequestMapping("/diseases")
public class DiseaseController {

    @Autowired
    private DiseaseRepo diseaseRepo;

    @GetMapping
    public List<Disease> getDiseases() {
        return diseaseRepo.findAll();
    }
}
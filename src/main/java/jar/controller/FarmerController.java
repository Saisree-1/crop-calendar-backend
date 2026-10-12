package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Farmer;
import jar.repo.FarmerRepo;

@RestController
@RequestMapping("/farmers")
public class FarmerController {

    @Autowired
    private FarmerRepo farmerRepo;

    @GetMapping
    public List<Farmer> getFarmers() {
        return farmerRepo.findAll();
    }

    @PostMapping
    public Farmer createFarmer(@RequestBody Farmer farmer) {
        return farmerRepo.save(farmer);
    }
}
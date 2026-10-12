package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.Location;
import jar.repo.LocationRepo;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepo locationRepo;

    @GetMapping
    public List<Location> getLocations() {
        return locationRepo.findAll();
    }
}
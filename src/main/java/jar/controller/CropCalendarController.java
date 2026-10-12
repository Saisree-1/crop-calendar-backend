package jar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import jar.model.ActivityRule;
import jar.model.CalendarEvent;
import jar.model.CropPlan;
import jar.service.CropCalendarService;

@RestController
@RequestMapping("/crop-calendar")
public class CropCalendarController {

    private final CropCalendarService service;

    public CropCalendarController(CropCalendarService service) {
        this.service = service;
    }

    @GetMapping
    public List<CropPlan> getCropPlans() {
        return service.getAllCropPlans();
    }

    @GetMapping("/rules")
    public List<ActivityRule> getRulesForCrop(
            @RequestParam Integer cropId) {
        return service.getRulesForCrop(cropId);
    }

    @PostMapping("/generate/{planId}")
    public List<CalendarEvent> generateCalendar(
            @PathVariable Long planId) {
        return service.generateCalendar(planId);
    }
}
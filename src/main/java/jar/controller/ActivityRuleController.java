package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.ActivityRule;
import jar.repo.ActivityRuleRepo;

@RestController
@RequestMapping("/activity-rules")
public class ActivityRuleController {

    @Autowired
    private ActivityRuleRepo activityRuleRepo;

    @GetMapping
    public List<ActivityRule> getActivityRules() {
        return activityRuleRepo.findAll();
    }
}
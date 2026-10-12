package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.WhatIfScenario;
import jar.repo.WhatIfScenarioRepo;
import jar.service.CropCalendarService;

@RestController
@RequestMapping("/what-if")
public class WhatIfScenarioController {

    @Autowired
    private WhatIfScenarioRepo whatIfScenarioRepo;

    @Autowired
    private CropCalendarService cropCalendarService;

    @GetMapping
    public List<WhatIfScenario> getWhatIfScenarios() {
        return whatIfScenarioRepo.findAll();
    }

    @PostMapping
    public WhatIfScenario createScenario(
            @RequestBody WhatIfScenario scenario) {

        return cropCalendarService.calculateWhatIf(
                scenario.getPlanId(),
                scenario.getScenarioType(),
                scenario.getDelayDays()
        );
    }
}
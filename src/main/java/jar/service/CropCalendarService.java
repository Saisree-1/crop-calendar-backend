package jar.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import jar.model.ActivityRule;
import jar.model.Alert;
import jar.model.CalendarEvent;
import jar.model.CropPlan;
import jar.model.CropStage;
import jar.model.Farm;
import jar.model.WeatherData;
import jar.model.WhatIfScenario;
import jar.repo.ActivityRuleRepo;
import jar.repo.AlertRepo;
import jar.repo.CalendarEventRepo;
import jar.repo.CropPlanRepo;
import jar.repo.CropStageRepo;
import jar.repo.FarmRepo;
import jar.repo.WeatherDataRepo;
import jar.repo.WhatIfScenarioRepo;

@Service
public class CropCalendarService {

    private final CropPlanRepo cropPlanRepo;
    private final ActivityRuleRepo activityRuleRepo;
    private final CropStageRepo cropStageRepo;
    private final CalendarEventRepo calendarEventRepo;
    private final WeatherDataRepo weatherDataRepo;
    private final AlertRepo alertRepo;
    private final FarmRepo farmRepo;
    private final WhatIfScenarioRepo whatIfScenarioRepo;

    public CropCalendarService(
            CropPlanRepo cropPlanRepo,
            ActivityRuleRepo activityRuleRepo,
            CropStageRepo cropStageRepo,
            CalendarEventRepo calendarEventRepo,
            WeatherDataRepo weatherDataRepo,
            AlertRepo alertRepo,
            FarmRepo farmRepo,
            WhatIfScenarioRepo whatIfScenarioRepo) {

        this.cropPlanRepo = cropPlanRepo;
        this.activityRuleRepo = activityRuleRepo;
        this.cropStageRepo = cropStageRepo;
        this.calendarEventRepo = calendarEventRepo;
        this.weatherDataRepo = weatherDataRepo;
        this.alertRepo = alertRepo;
        this.farmRepo = farmRepo;
        this.whatIfScenarioRepo = whatIfScenarioRepo;
    }

    public List<CropPlan> getAllCropPlans() {
        return cropPlanRepo.findAll();
    }

    public List<ActivityRule> getRulesForCrop(Integer cropId) {
        return activityRuleRepo.findByCropId(cropId);
    }

    public Optional<CropStage> getStage(Integer stageId) {
        return cropStageRepo.findById(stageId);
    }

    public List<CalendarEvent> generateCalendar(Long planId) {

        // 1. Find crop plan
        CropPlan plan = cropPlanRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Crop plan not found"));

        // 2. Find farm
        Farm farm = farmRepo.findById(plan.getFarmId())
                .orElseThrow(() -> new RuntimeException("Farm not found"));

        // 3. Get actual location from farm
        Integer locationId = farm.getLocationId();

        // 4. Get rules for crop
        List<ActivityRule> rules =
                activityRuleRepo.findByCropId(plan.getCropId());

        List<CalendarEvent> events = new ArrayList<>();

        // Delete old events before generating new ones
        calendarEventRepo.deleteAll(
                calendarEventRepo.findByPlanId(plan.getPlanId())
        );

        Set<String> processedRules = new HashSet<>();

        // 5. Process every rule
        for (ActivityRule rule : rules) {

            String ruleKey =
                    rule.getActivityType() + "-" +
                    rule.getStageId() + "-" +
                    rule.getOffsetDay() + "-" +
                    rule.getMinRainMm() + "-" +
                    rule.getMaxRainMm();

            if (processedRules.contains(ruleKey)) {
                continue;
            }

            processedRules.add(ruleKey);

            // 6. Calculate planned date
            LocalDate plannedDate =
                    plan.getSowingDate().plusDays(rule.getOffsetDay());

            LocalDate adjustedDate = plannedDate;

            String riskLevel = "LOW";
            Integer confidencePct = 90;
            String reasonKey = "RULE_BASED_SCHEDULE";

            // 7. Find weather for this farm's location and date
            Optional<WeatherData> weatherOptional =
                    weatherDataRepo.findAll()
                            .stream()
                            .filter(weather ->
                                    weather.getLocationId() != null
                                    && weather.getLocationId().equals(locationId)
                                    && weather.getWeatherDate() != null
                                    && weather.getWeatherDate().equals(plannedDate))
                            .findFirst();

            // 8. Check climate conditions
            if (weatherOptional.isPresent()) {

                WeatherData weather = weatherOptional.get();

                BigDecimal rainfall = weather.getRainfallMm();
                BigDecimal maxTemperature = weather.getTempMaxC();

                // =========================================
                // EXCESS RAINFALL
                // =========================================
                if (rule.getMaxRainMm() != null
                        && rainfall != null
                        && rainfall.compareTo(
                                BigDecimal.valueOf(rule.getMaxRainMm())) > 0) {

                    riskLevel = "HIGH";
                    confidencePct = 75;
                    reasonKey = "EXCESS_RAIN";

                    adjustedDate = plannedDate.plusDays(2);

                    createAlertIfNotExists(
                            plan.getPlanId(),
                            "EXCESS_RAIN",
                            "HIGH",
                            "EXCESS_RAIN_ACTIVITY_DELAY");
                }

                // =========================================
                // LOW RAINFALL
                // =========================================
                else if (rule.getMinRainMm() != null
                        && rainfall != null
                        && rainfall.compareTo(
                                BigDecimal.valueOf(rule.getMinRainMm())) < 0) {

                    riskLevel = "HIGH";
                    confidencePct = 75;
                    reasonKey = "LOW_RAINFALL";

                    adjustedDate = plannedDate.plusDays(2);

                    createAlertIfNotExists(
                            plan.getPlanId(),
                            "DROUGHT",
                            "HIGH",
                            "LOW_RAINFALL_ACTIVITY_DELAY");
                }

                // =========================================
                // HEAT WAVE
                // =========================================
                else if (maxTemperature != null
                        && maxTemperature.compareTo(
                                BigDecimal.valueOf(40)) > 0) {

                    riskLevel = "HIGH";
                    confidencePct = 70;
                    reasonKey = "HEAT_WAVE";

                    adjustedDate = plannedDate.plusDays(1);

                    createAlertIfNotExists(
                            plan.getPlanId(),
                            "HEAT",
                            "HIGH",
                            "HEAT_WAVE_ACTIVITY_ADJUSTMENT");
                }
            }

            // 9. Create calendar event
            CalendarEvent event = new CalendarEvent();

            event.setPlanId(plan.getPlanId());
            event.setRuleId(rule.getRuleId());
            event.setActivityType(rule.getActivityType());
            event.setPlannedDate(plannedDate);
            event.setAdjustedDate(adjustedDate);

            if (adjustedDate.equals(plannedDate)) {
                event.setStatus("PENDING");
            } else {
                event.setStatus("RESCHEDULED");
            }

            event.setRiskLevel(riskLevel);
            event.setConfidencePct(confidencePct);
            event.setReasonKey(reasonKey);

            events.add(event);
        }

        // 10. Save events
        return calendarEventRepo.saveAll(events);
    }

    // =====================================================
    // CREATE ALERT WITHOUT DUPLICATES
    // =====================================================
    private void createAlertIfNotExists(
            Long planId,
            String alertType,
            String severity,
            String messageKey) {

        boolean alertAlreadyExists = alertRepo.findAll()
                .stream()
                .anyMatch(existingAlert ->
                        existingAlert.getPlanId() != null
                        && existingAlert.getPlanId().equals(planId)
                        && alertType.equals(existingAlert.getAlertType())
                        && messageKey.equals(existingAlert.getMessageKey())
                        && "PENDING".equals(existingAlert.getStatus()));

        if (!alertAlreadyExists) {

            Alert alert = new Alert();

            alert.setPlanId(planId);
            alert.setAlertType(alertType);
            alert.setSeverity(severity);
            alert.setMessageKey(messageKey);
            alert.setChannel("APP");
            alert.setStatus("PENDING");
            alert.setCreatedAt(LocalDateTime.now());

            alertRepo.save(alert);
        }
    }

    // =====================================================
    // WHAT-IF SCENARIO CALCULATION
    // =====================================================
    public WhatIfScenario calculateWhatIf(
            Long planId,
            String scenarioType,
            Integer delayDays) {

        // Find crop plan
        cropPlanRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Crop plan not found"));

        // Get current calendar events
        List<CalendarEvent> currentEvents =
                calendarEventRepo.findByPlanId(planId);

        // Create result JSON
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < currentEvents.size(); i++) {

            CalendarEvent event = currentEvents.get(i);

            LocalDate originalDate = event.getAdjustedDate();

            LocalDate alternativeDate =
                    originalDate.plusDays(delayDays);

            result.append("{")
                    .append("\"activityType\":\"")
                    .append(event.getActivityType())
                    .append("\",")
                    .append("\"originalDate\":\"")
                    .append(originalDate)
                    .append("\",")
                    .append("\"alternativeDate\":\"")
                    .append(alternativeDate)
                    .append("\"")
                    .append("}");

            if (i < currentEvents.size() - 1) {
                result.append(",");
            }
        }

        result.append("]");

        // Save the What-If scenario
        WhatIfScenario scenario = new WhatIfScenario();

        scenario.setPlanId(planId);
        scenario.setScenarioType(scenarioType);
        scenario.setDelayDays(delayDays);
        scenario.setResultJson(result.toString());
        scenario.setCreatedAt(LocalDateTime.now());

        return whatIfScenarioRepo.save(scenario);
    }
}
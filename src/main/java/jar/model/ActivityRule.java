package jar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "activity_rules")
public class ActivityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;

    @Column(name = "crop_id")
    private Integer cropId;

    @Column(name = "stage_id")
    private Integer stageId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "offset_day")
    private Integer offsetDay;

    @Column(name = "min_rain_mm")
    private Double minRainMm;

    @Column(name = "max_rain_mm")
    private Double maxRainMm;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getOffsetDay() {
        return offsetDay;
    }

    public void setOffsetDay(Integer offsetDay) {
        this.offsetDay = offsetDay;
    }

    public Double getMinRainMm() {
        return minRainMm;
    }

    public void setMinRainMm(Double minRainMm) {
        this.minRainMm = minRainMm;
    }

    public Double getMaxRainMm() {
        return maxRainMm;
    }

    public void setMaxRainMm(Double maxRainMm) {
        this.maxRainMm = maxRainMm;
    }
}
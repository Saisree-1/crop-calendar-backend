package jar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "crop_stages")
public class CropStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stage_id")
    private Integer stageId;

    @Column(name = "crop_id")
    private Integer cropId;

    @Column(name = "stage_name")
    private String name;

    @Column(name = "start_day")
    private Integer dayFrom;

    @Column(name = "end_day")
    private Integer dayTo;

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDayFrom() {
        return dayFrom;
    }

    public void setDayFrom(Integer dayFrom) {
        this.dayFrom = dayFrom;
    }

    public Integer getDayTo() {
        return dayTo;
    }

    public void setDayTo(Integer dayTo) {
        this.dayTo = dayTo;
    }
}
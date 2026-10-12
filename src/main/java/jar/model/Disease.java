package jar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "diseases")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diseaseId;

    private Integer cropId;
    private String name;
    private String symptomsKey;
    private String organicTreatmentKey;
    private String chemicalTreatmentKey;

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
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

    public String getSymptomsKey() {
        return symptomsKey;
    }

    public void setSymptomsKey(String symptomsKey) {
        this.symptomsKey = symptomsKey;
    }

    public String getOrganicTreatmentKey() {
        return organicTreatmentKey;
    }

    public void setOrganicTreatmentKey(String organicTreatmentKey) {
        this.organicTreatmentKey = organicTreatmentKey;
    }

    public String getChemicalTreatmentKey() {
        return chemicalTreatmentKey;
    }

    public void setChemicalTreatmentKey(String chemicalTreatmentKey) {
        this.chemicalTreatmentKey = chemicalTreatmentKey;
    }
}
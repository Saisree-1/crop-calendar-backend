package jar.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "disease_scans")
public class DiseaseScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scanId;

    private Long planId;
    private String imagePath;
    private Integer diseaseId;
    private Integer confidencePct;
    private Boolean scannedOffline;
    private LocalDateTime scannedAt;

    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Integer getConfidencePct() {
        return confidencePct;
    }

    public void setConfidencePct(Integer confidencePct) {
        this.confidencePct = confidencePct;
    }

    public Boolean getScannedOffline() {
        return scannedOffline;
    }

    public void setScannedOffline(Boolean scannedOffline) {
        this.scannedOffline = scannedOffline;
    }

    public LocalDateTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }
}
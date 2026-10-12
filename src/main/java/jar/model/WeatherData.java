package jar.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weatherId;

    private Integer locationId;
    private LocalDate weatherDate;
    private BigDecimal rainfallMm;
    private BigDecimal tempMinC;
    private BigDecimal tempMaxC;
    private BigDecimal humidityPct;
    private String dataType;

    public Long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Long weatherId) {
        this.weatherId = weatherId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public LocalDate getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(LocalDate weatherDate) {
        this.weatherDate = weatherDate;
    }

    public BigDecimal getRainfallMm() {
        return rainfallMm;
    }

    public void setRainfallMm(BigDecimal rainfallMm) {
        this.rainfallMm = rainfallMm;
    }

    public BigDecimal getTempMinC() {
        return tempMinC;
    }

    public void setTempMinC(BigDecimal tempMinC) {
        this.tempMinC = tempMinC;
    }

    public BigDecimal getTempMaxC() {
        return tempMaxC;
    }

    public void setTempMaxC(BigDecimal tempMaxC) {
        this.tempMaxC = tempMaxC;
    }

    public BigDecimal getHumidityPct() {
        return humidityPct;
    }

    public void setHumidityPct(BigDecimal humidityPct) {
        this.humidityPct = humidityPct;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
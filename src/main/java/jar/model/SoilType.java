package jar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "soil_types")
public class SoilType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer soilTypeId;

    private String name;

    public Integer getSoilTypeId() {
        return soilTypeId;
    }

    public void setSoilTypeId(Integer soilTypeId) {
        this.soilTypeId = soilTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
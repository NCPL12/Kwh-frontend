package ncpl.bms.reports.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class FloorToEnergyMeterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer floorId;
    private String name;

    // Default Constructor
    public FloorToEnergyMeterDTO() {}

    // Parameterized Constructor
    public FloorToEnergyMeterDTO(Integer id, Integer floorId, String name) {
        this.id = id;
        this.floorId = floorId;
        this.name = name;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFloorId() { return floorId; }
    public void setFloorId(Integer floorId) { this.floorId = floorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloorToEnergyMeterDTO that = (FloorToEnergyMeterDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(floorId, that.floorId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, floorId, name);
    }

    @Override
    public String toString() {
        return "FloorToEnergyMeterDTO{" +
                "id=" + id +
                ", floorId=" + floorId +
                ", name='" + name + '\'' +
                '}';
    }
}

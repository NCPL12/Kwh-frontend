package ncpl.bms.reports.service;

import ncpl.bms.reports.model.dto.FloorToEnergyMeterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FloorToEnergyMeterService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Fetches floor details by ID.
     */
    public FloorToEnergyMeterDTO getFloorDetailsById(int floorId) {
        String sql = "SELECT id, name FROM floors WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{floorId}, (rs, rowNum) ->
                    new FloorToEnergyMeterDTO(rs.getInt("id"), floorId, rs.getString("name"))
            );
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Floor not found with ID: " + floorId);
        }
    }

    /**
     * Retrieves all active energy meters assigned to floors.
     */
    public List<FloorToEnergyMeterDTO> getAllFloorEnergyMeters() {
        String sql = "SELECT id, floor_id, name FROM floor_to_energy_meter_relation";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new FloorToEnergyMeterDTO(rs.getInt("id"), rs.getInt("floor_id"), rs.getString("name"))
        );
    }

    /**
     * Adds a new floor-energy meter relation.
     */
    public FloorToEnergyMeterDTO addFloorEnergyMeter(FloorToEnergyMeterDTO floorEnergyMeter) {
        String sql = "INSERT INTO floor_to_energy_meter_relation (floor_id, name) VALUES (?, ?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, new Object[]{floorEnergyMeter.getFloorId(), floorEnergyMeter.getName()},
                (rs, rowNum) -> new FloorToEnergyMeterDTO(rs.getInt("id"), floorEnergyMeter.getFloorId(), floorEnergyMeter.getName()));
    }

    /**
     * Updates an existing floor-energy meter relation.
     */
    public void updateFloorEnergyMeter(FloorToEnergyMeterDTO floorEnergyMeter) {
        String sql = "UPDATE floor_to_energy_meter_relation SET name = ?, floor_id = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, floorEnergyMeter.getName(), floorEnergyMeter.getFloorId(), floorEnergyMeter.getId());
        if (rowsAffected == 0) {
            throw new RuntimeException("Floor-energy meter relation not found with ID: " + floorEnergyMeter.getId());
        }
    }

    /**
     * Deletes a floor-energy meter relation by ID.
     */
    public void deleteFloorEnergyMeterById(int id) {
        String sql = "DELETE FROM floor_to_energy_meter_relation WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new RuntimeException("Floor-energy meter relation not found with ID: " + id);
        }
    }

    /**
     * Fetches energy meters assigned to a floor and calculates energy usage.
     */
    public Map<String, Double> getEnergyUsageForFloor(int floorId, String month, String year) {
        String sql = "SELECT name FROM floor_to_energy_meter_relation WHERE floor_id = ?";
        List<String> energyMeters = jdbcTemplate.query(sql, new Object[]{floorId}, (rs, rowNum) -> rs.getString("name"));

        // Separate energy meters into EB and DG categories
        List<String> ebMeters = energyMeters.stream()
                .filter(name -> name.endsWith("_eb_kwh"))
                .collect(Collectors.toList());

        List<String> dgMeters = energyMeters.stream()
                .filter(name -> name.endsWith("_dg_kwh"))
                .collect(Collectors.toList());

        // Convert month and year to YYYY-MM format
        String monthYear = year + "-" + String.format("%02d", Integer.parseInt(month));

        double totalEbKwh = 0, totalDgKwh = 0;
        if (!ebMeters.isEmpty()) {
            totalEbKwh = 0; // Replace with actual query logic
        }
        if (!dgMeters.isEmpty()) {
            totalDgKwh = 0; // Replace with actual query logic
        }

        return Map.of("totalEbKwh", totalEbKwh, "totalDgKwh", totalDgKwh);
    }
}

package ncpl.bms.reports.controller;

import ncpl.bms.reports.model.dto.FloorToEnergyMeterDTO;
import ncpl.bms.reports.service.FloorToEnergyMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
@CrossOrigin(origins = "http://localhost:4200")
public class FloorToEnergyMeterController {

    @Autowired
    private FloorToEnergyMeterService floorToEnergyMeterService;

    @GetMapping("/get-all-floor-energy-meters")
    public List<FloorToEnergyMeterDTO> getAllFloorEnergyMeters() {
        return floorToEnergyMeterService.getAllFloorEnergyMeters();
    }

    @PostMapping("/add-floor-energy-meter")
    public ResponseEntity<FloorToEnergyMeterDTO> addFloorEnergyMeter(@RequestBody FloorToEnergyMeterDTO dto) {
        FloorToEnergyMeterDTO savedRelation = floorToEnergyMeterService.addFloorEnergyMeter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRelation);
    }

    @PutMapping("/update-floor-energy-meter")
    public ResponseEntity<String> updateFloorEnergyMeter(@RequestBody FloorToEnergyMeterDTO dto) {
        floorToEnergyMeterService.updateFloorEnergyMeter(dto);
        return ResponseEntity.ok("Floor-energy meter relation updated successfully.");
    }

    @DeleteMapping("/delete-floor-energy-meter/{id}")
    public ResponseEntity<String> deleteFloorEnergyMeter(@PathVariable int id) {
        floorToEnergyMeterService.deleteFloorEnergyMeterById(id);
        return ResponseEntity.ok("Relation deleted successfully.");
    }

    @GetMapping("/energy-usage/{floorId}/{month}/{year}")
    public ResponseEntity<Map<String, Double>> getEnergyUsageForFloor(@PathVariable int floorId, @PathVariable String month, @PathVariable String year) {
        return ResponseEntity.ok(floorToEnergyMeterService.getEnergyUsageForFloor(floorId, month, year));
    }
}

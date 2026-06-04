package de.easyscoot.contoller;

import de.easyscoot.model.EScooter;
import de.easyscoot.model.Maintenancestatus;
import de.easyscoot.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
public class ServiceArbeiterController {

    private final MaintenanceService maintenanceService;

    public ServiceArbeiterController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/scooterList")
    public ResponseEntity<List<EScooter>> getAllScooter() {
        try {
            List<EScooter> scooters = maintenanceService.getAllEScooter();
            return ResponseEntity.ok(scooters);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/setWartung")
    public ResponseEntity<String> updateMaintenanceMode(@RequestParam("id") String id, @RequestParam("status") Maintenancestatus status) {
        try {
            maintenanceService.updateMaintenanceMode(id, status);
            return ResponseEntity.ok("Erfolgreich in Wartung gesetzt");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/scooterListFilter")
    public ResponseEntity<List<EScooter>> getScootersLowBattery(@RequestParam("thresholdPercentage") double thresholdPercentage) {
        try {
            List<EScooter> scooters1 = maintenanceService.getScootersLowBattery(thresholdPercentage);
            return ResponseEntity.ok(scooters1);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

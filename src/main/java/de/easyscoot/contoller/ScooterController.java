package de.easyscoot.contoller;

import de.easyscoot.model.Availability;
import de.easyscoot.model.EScooter;
import de.easyscoot.service.EScooterService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ScooterController {
    private final EScooterService escooterService;

    public ScooterController(EScooterService escooterService) {
        this.escooterService = escooterService;
    }

    @GetMapping("/scooters/available")
    public List<EScooter> getScooters() {
         List<EScooter> scooters = escooterService.getAllScooters();
         List<EScooter> scootersAvailability = new ArrayList<>();
         for (EScooter escooter : scooters) {
             if (escooter.getAvailability() == Availability.NICHT_IN_BENUTZUNG) {
                 scootersAvailability.add(escooter);
             }
         }
         return scootersAvailability;
    }
}

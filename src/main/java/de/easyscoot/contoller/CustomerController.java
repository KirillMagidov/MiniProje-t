package de.easyscoot.contoller;

import de.easyscoot.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/{customerId}/deposit")
    public ResponseEntity<?> depositMoney(
            @PathVariable String customerId,
            @RequestParam Double deposit) {
        try {
            Double newBalance = customerService.depositMoneyById(customerId, deposit);
            return ResponseEntity.ok("Neues Guthaben: " + newBalance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
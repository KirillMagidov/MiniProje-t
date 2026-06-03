package de.easyscoot.contoller;

import de.easyscoot.model.Customer;
import de.easyscoot.model.LoginRequest;
import de.easyscoot.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody Customer customer) {
        try {
            service.createAccount(customer);
            return ResponseEntity.ok("Account erfolgreich erstellt!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Customer loggedInCustomer = service.logIn(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(loggedInCustomer.getCustomerId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody LoginRequest loginRequest) {
        try {
            service.logIn(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok("Verifiziert"); //wenn keine exception
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getCustomer")
    public ResponseEntity<Customer> getCustomer(@RequestParam("customerId") String customerId) {
        try {
            Customer customer = service.getCustomer(customerId);
            if (customer == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<Customer> changeCustomerData(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("customerId") String customerId,
            @RequestBody Customer newCustomer) {
        try {
            service.changeCustomerData(email, password, customerId, newCustomer);
            return ResponseEntity.ok(newCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount (@RequestParam String email, @RequestParam String password, @RequestParam String customerId) {
        try {
            service.deleteAccount(email, password, customerId);
            return ResponseEntity.ok("Account wurde erfolgreich gelöscht");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


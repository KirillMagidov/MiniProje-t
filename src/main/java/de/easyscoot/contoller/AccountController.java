package de.easyscoot.contoller;

import de.easyscoot.model.Customer;
import de.easyscoot.model.LoginRequest;
import de.easyscoot.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AccountController {

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody Customer customer) {

        try {
            AccountService service = new AccountService(customer);

            service.createAccount(customer);

            return ResponseEntity.ok("Account erfolgreich erstellt!");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            AccountService service = new AccountService(null);
            service.logIn(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok("Login erfolgreich");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

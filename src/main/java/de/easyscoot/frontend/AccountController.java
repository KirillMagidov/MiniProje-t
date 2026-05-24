package de.easyscoot.frontend;

import de.easyscoot.model.Customer;
import de.easyscoot.service.AccountService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class AccountController {

    @PostMapping("/createAccount")
    public String createAccount(@RequestBody Customer customer) {

        try {
            AccountService service = new AccountService(customer);

            service.createAccount(customer);

            return "Account erfolgreich erstellt!";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}

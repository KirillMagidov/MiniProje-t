package de.easyscoot.contoller;

import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import de.easyscoot.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }
    @PostMapping("/{customerId}/deposit")
    public ResponseEntity<?> depositMoney(
            @PathVariable String customerId,
             @RequestParam Double deposit) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Kunde nicht gefunden");
        }
        customerService.depositMoney(customer, deposit);
        customerRepository.saveCustomer(customer);
        return ResponseEntity.ok("Neues Guthaben: "+ customer.getCredit());
    }
}

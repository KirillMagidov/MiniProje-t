package de.easyscoot.service;

import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class AccountService implements IAccountService {

    private final CustomerRepository repo;
    private final ValidationService validation;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AccountService(CustomerRepository repo, ValidationService validation) {
        this.repo = repo;
        this.validation = validation;
    }

    @Override
    public void createAccount(Customer customer) {

        if (repo.emailExists(customer.getEmail())) {
            throw new RuntimeException("Account existiert bereits");
        }
        if (!validation.isValid(customer.getEmail())) {
            throw new RuntimeException("Invalid email");
        }
        if (!validation.isValidAdresse(customer.getStreet(), customer.getStreetNumber(), customer.getPlz(), customer.getLocation())) {
            throw new RuntimeException("Invalid Addresse");
        }
        if (!validation.isValidPassword(customer.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        customer.setCustomerId(generateCustomerId());

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        repo.saveCustomer(customer);
    }

    public boolean logIn(String email, String password) {
        Customer savedCustomer = repo.getCustomer(email);

        if (savedCustomer == null) {
            throw new RuntimeException("Account existiert noch nicht oder E-Mail ist falsch");
        }

        if (passwordEncoder.matches(password, savedCustomer.getPassword())) {
            return true;
        } else {
            throw new RuntimeException("Passwort ist falsch");
        }
    }

    //ID_GENERATOR
    private String generateCustomerId() {
        Random random = new Random();
        int id = 100000 + random.nextInt(900000);
        return String.valueOf(id);
    }
}
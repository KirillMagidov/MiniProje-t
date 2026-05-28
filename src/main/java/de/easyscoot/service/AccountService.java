package de.easyscoot.service;

import de.easyscoot.model.Customer;
import de.easyscoot.model.LoginRequest;
import de.easyscoot.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AccountService implements IAccountService {

    private final CustomerRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ValidationService validation;


    public AccountService(CustomerRepository repo, ValidationService validation) {
        this.repo = repo;
        this.validation = validation;
    }

/*
    @Override
    public boolean proofEmailExists(String email) {
        return repo.emailExists(email);
    }

 */

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

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        repo.saveCustomer(customer);
    }

    public boolean logIn(String email, String password) {
        Customer savedCustomers = repo.getCustomer(email);
        if (savedCustomers == null) {
            throw new RuntimeException("Account existiert noch nicht");
        }

        if (passwordEncoder.matches(password, savedCustomers.getPassword())) {
            return true;
        } else {
            throw new RuntimeException("Passowrt ist falsch");
        }
    }
}

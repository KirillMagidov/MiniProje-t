package de.easyscoot.service;

import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.util.List;

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

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        repo.saveCustomer(customer);
    }

    public boolean logIn(String password, Customer customer) {
        if (!repo.emailExists(customer.getEmail())) {
            throw new RuntimeException("Accounts existiert noch nicht");
        } else {
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return true;
            }
            throw new RuntimeException("Passwort ist falsch");
        }
    }
}

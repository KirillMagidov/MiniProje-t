package de.easyscoot.service;

import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.*;
import java.util.List;


public class AccountService implements IAccountService {

    private CustomerRepository repo = new CustomerRepository();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ValidationService validation = new ValidationService();

    private Customer customer;


    public AccountService(Customer customer) {
        this.customer = customer;
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

    public boolean logIn(String password) {
        if (!repo.emailExists(customer.getEmail())) {
            throw new RuntimeException("Accounts existiert noch nicht");
        } else {
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return true;
            }
            throw new RuntimeException("Passowrt ist falsch");
        }
    }
}

package de.easyscoot.service;

import de.easyscoot.model.*;
import de.easyscoot.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Double depositMoneyById(String customerId, Double deposit) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new RuntimeException("Kunde nicht gefunden");
        }
        depositMoney(customer, deposit);
        return customer.getCredit();
    }

    @Override
    public void depositMoney(Customer customer, Double deposit) {
        customer.setCredit(customer.getCredit() + deposit);
        customerRepository.saveCustomer(customer);
    }

    @Override
    public void debitMoney(Customer customer, Double debit) {
        customer.setCredit(customer.getCredit() - debit);
        customerRepository.saveCustomer(customer);
    }

    @Override
    public void enoughMoneyForARide(Customer customer) {
        if (customer.getCredit() < 0.00) {
            throw new RuntimeException("Nicht genügend Guthaben vorhanden, bezahle zunächst deine noch offenen Fahrten und stocke das Guthaben auf 5 Euro auf.");
        } else if (customer.getCredit() < 5.00) {
            throw new RuntimeException("Nicht genügend Guthaben vorhanden. Du brauchst mindestens 5 Euro, um eine Fahrt zu starten.");
        }
    }
}
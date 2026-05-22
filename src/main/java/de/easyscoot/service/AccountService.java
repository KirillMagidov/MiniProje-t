package de.easyscoot.service;
import de.easyscoot.model.Customer;
import de.easyscoot.repository.CustomerRepository;

public class AccountService implements IAccountService {

    private CustomerRepository repo = new CustomerRepository();

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
    public void createAccount (Customer customer) {
        if (!repo.emailExists(customer.getEmail())) {
            repo.saveCustomer(customer);
        }
        else {
            throw new RuntimeException("Account existiert bereits");
        }
    }

    public void logIn () {
    }
}

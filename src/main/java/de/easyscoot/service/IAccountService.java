package de.easyscoot.service;

import de.easyscoot.model.Customer;

public interface IAccountService {

    void createAccount(Customer customer);

    Customer logIn(String email, String password);

    void deleteAccount(String email, String password, String customerId);

    void changeCustomerData(String email, String password, String customerId, Customer newCustomer);

    Customer getCustomer(String customerId);

}
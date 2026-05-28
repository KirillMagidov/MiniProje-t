package de.easyscoot.repository;

import de.easyscoot.model.Customer;

import java.util.List;

public interface ICustomerRepository {

    List<Customer> getAllCustomers();

    void saveCustomer(Customer customer);

    boolean emailExists(String email);
}

package de.easyscoot.repository;

import de.easyscoot.model.Customer;

import java.util.List;

public interface ICustomerRepository {

    public List<Customer> getAllCustomers();

    public void saveCustomer(Customer customer);

    public boolean emailExists(String email);
}

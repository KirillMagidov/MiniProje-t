package de.easyscoot.service;
import de.easyscoot.model.Customer;

public interface IAccountService {

    //boolean proofEmailExists (String email);

    //legt Konto an und speichert Daten des Kunden
    void createAccount (Customer customer);

    Customer logIn(String email, String password);


    void deleteAccount (String email, String password, String customerId);

    void changeCustomerData (String email, String password,String customerId, Customer newCustomer);
}


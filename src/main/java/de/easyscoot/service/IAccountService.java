package de.easyscoot.service;
import de.easyscoot.model.Customer;

public interface IAccountService {

    //boolean proofEmailExists (String email);

    //legt Konto an und speichert Daten des Kunden
    void createAccount (Customer customer);

    void logIn();

}


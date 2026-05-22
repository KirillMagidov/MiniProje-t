package de.easyscoot.service;

import de.easyscoot.model.EScooter;
import de.easyscoot.model.Customer;

public interface ICustomerService {

    EScooter searchEScooter(Customer customer);

    void bookEScooter(Customer customer, EScooter escooter);
//test
}

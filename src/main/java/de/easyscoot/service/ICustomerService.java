package de.easyscoot.service;

import de.easyscoot.model.Booking;
import de.easyscoot.model.EScooter;
import de.easyscoot.model.Customer;

public interface ICustomerService {

    EScooter searchEScooter();

    void bookEScooter(Customer customer, EScooter escooter);

    void stopEScooter(Booking booking);
//test
}

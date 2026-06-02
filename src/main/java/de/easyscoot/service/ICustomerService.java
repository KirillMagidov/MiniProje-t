package de.easyscoot.service;

import de.easyscoot.model.Booking;
import de.easyscoot.model.EScooter;
import de.easyscoot.model.Customer;

public interface ICustomerService {

    void depositMoney(Customer customer, Double deposit);

    void debitMoney(Customer customer, Double debit);

    void enoughMoneyForARide(Customer customer);
}

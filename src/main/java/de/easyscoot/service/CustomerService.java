package de.easyscoot.service;

import de.easyscoot.model.*;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {




    @Override
    public void depositMoney(Customer customer, Double deposit) {
        customer.setCredit(customer.getCredit() + deposit);
    }

    @Override
    public void debitMoney(Customer customer, Double debit) {
        if(debit > customer.getCredit()){
            throw new RuntimeException("Zu wenig Guthaben verfügbar");
        }
        customer.setCredit(customer.getCredit() - debit);
    }

    @Override
    public void enoughMoneyForARide(Customer customer) {
       if(customer.getCredit() < 5.00 &&  customer.getCredit() > 0.00){
           throw new RuntimeException("Nicht genügend Guthaben vorhanden");

       }
       if(customer.getCredit() < 0.00){
           throw new RuntimeException("Nicht genügend Guthaben vorhanden, bezahle zunächst deine noch offen Fahrten und stocke das Guthaben auf 5 Euro auf");
       }
    }
}
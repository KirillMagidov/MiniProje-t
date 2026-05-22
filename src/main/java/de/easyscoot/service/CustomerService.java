package de.easyscoot.service;

import de.easyscoot.model.Availability;
import de.easyscoot.model.Customer;
import de.easyscoot.model.EScooter;
import de.easyscoot.repository.CustomerRepository;
import de.easyscoot.repository.IScooterRepository;

import java.util.List;


public class CustomerService implements ICustomerService{

    private CustomerRepository customerRepo = new CustomerRepository();

    private Customer customer;

    private EScooter chosenEscooter;

    private IScooterRepository repository;

    public CustomerService(Customer customer, EScooter escooter) {
        this.customer = customer;
        this.chosenEscooter = escooter;
    }

    public IScooterRepository getRepository() {
        return repository;
    }

    @Override
    public void bookEScooter(Customer customer, EScooter chosenEScooter) {
        chosenEScooter = searchEScooter(customer);
        if  (chosenEScooter != null) {
            throw new RuntimeException("Kein E-Scooter verfügbar");
        }
    }

    @Override
    public EScooter searchEScooter(Customer customer) {
        List<EScooter> escooters = repository.findAll();
        for (EScooter escooter : escooters) {
            if(escooter.getAvailability() == Availability.NOT_IN_USE){
                escooter.setAvailability(Availability.IN_USE);
                return escooter;                                            // in Json-datei einspeisen
            }
        }
        return null;
    }
}

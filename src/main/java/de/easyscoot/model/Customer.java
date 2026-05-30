package de.easyscoot.model;

import org.springframework.stereotype.Service;

@Service
public class Customer extends User {
    private String customerId;
    private String street;
    private Integer streetNumber;
    private String location;
    private Integer plz;

    public Customer() {

    }


    public Customer(String forename, String name, String street,
                    Integer streetNumber, String location,
                    Integer plz, String email, String password) {

        super(forename, name, email, password);
        this.street = street;
        this.streetNumber = streetNumber;
        this.location = location;
        this.plz = plz;
    }


    public String getStreet() {
        return street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public String getLocation() {
        return location;
    }

    public Integer getPlz() {
        return plz;
    }


    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}

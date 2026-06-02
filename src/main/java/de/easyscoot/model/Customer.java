package de.easyscoot.model;

public class Customer extends User {
    private String customerId;
    private String street;
    private Integer streetNumber;
    private String location;
    private Integer plz;
    private Double credit = 10.0;

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
        this.credit = 10.0;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public Double getCredit() {
        if (this.credit == null) {
            this.credit = 0.0;
        }
        return this.credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
package de.easyscoot.model;

public class Customer {

    private String foreName;
    private String name;
    private String street;
    private Integer streetNumber;
    private String location;
    private Integer plz;
    private String email;
    private String password;

    public Customer () {

    }



    public Customer(String foreName, String name, String street,
                    Integer streetNumber, String location,
                    Integer plz, String email, String password) {

        this.foreName = foreName;
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.location = location;
        this.plz = plz;
        this.email = email;
        this. password = password;
    }


    public String getForeName() {
        return foreName;
    }

    public String getName() {
        return name;
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

    public String getEmail() {
        return email;
    }

    public String getPassword (){
        return password;
    }


    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword (String password) {
        this.password = password;
    }
}

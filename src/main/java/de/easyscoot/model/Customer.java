package de.easyscoot.model;

public class Customer {
    private String foreName;
    private String Name;
    private String Street;
    private Integer StreetNumber;
    private String Location;
    private Integer Plz;
    private String Email;

    public Customer(String foreName, String Name, String Street, Integer StreetNumber, String Location, Integer Plz, String Email) {
        this.foreName = foreName;
        this.Name = Name;
        this.Street = Street;
        this.StreetNumber = StreetNumber;
        this.Location = Location;
        this.Plz = Plz;
        this.Email = Email;

    }

    public String getForeName () {
        return foreName;
    }

    public String getName() {
        return Name;
    }

    public String getStreet () {
        return Street;
    }

    public Integer getStreetNumber() {
        return StreetNumber;
    }

    public String getLocation() {
        return Location;
    }

    public Integer getPlz() {
        return Plz;
    }


    public String getEmail () {
        return Email;
    }


}

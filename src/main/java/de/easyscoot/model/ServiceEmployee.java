package de.easyscoot.model;

public class ServiceEmployee {
    private String foreName;
    private String name;
    private String email;
    private String password;

    public ServiceEmployee() {

    }

    public ServiceEmployee(String forname, String name, String email, String password) {
        this.foreName = forname;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getForeName() {
        return foreName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setForename(String forename) {
        this.foreName = forename;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

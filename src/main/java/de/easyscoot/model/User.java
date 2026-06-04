package de.easyscoot.model;

public abstract class User {
    private String foreName;
    private String name;
    private String email;
    private String password;

    public User() {

    }

    public User(String forename, String name, String email, String password) {
        this.foreName = forename;
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


    public void setForeName(String forename) {
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



package de.easyscoot.service;

public interface IValidationService {
    public boolean isValid(String email);

    public boolean isValidAdresse(String street, Integer streetNumber, Integer plz, String location);

    public boolean isValidPassword(String password);
}

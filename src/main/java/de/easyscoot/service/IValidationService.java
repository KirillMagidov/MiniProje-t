package de.easyscoot.service;

public interface IValidationService {
    boolean isValid(String email);

    boolean isValidAdresse(String street, Integer streetNumber, Integer plz, String location);

    boolean isValidPassword(String password);
}

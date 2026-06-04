package de.easyscoot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidationService implements IValidationService{

    @Value("${validation.email.api-key}")
    private String apiKey;

    @Value("${validation.email.base-url}")
    private String baseUrl;

    @Value("${validation.address.api-key}")
    private String loqateKey;

    @Value("${validation.address.url}")
    private String loqateUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean isValid(String email) {
        String url = baseUrl + "?api_key=" + apiKey + "&email=" + email;
        try {
            String response = restTemplate.getForObject(url, String.class);
            return response != null && response.contains("\"deliverable\"");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValidAdresse(String street, Integer streetNumber, Integer plz, String location) {
        String urlTemplate = loqateUrl + "?Key={key}&Countries=DE&Text={text}";
        String addressQuery = street + " " + streetNumber + ", " + plz + " " + location;
        try {
            String response = restTemplate.getForObject(urlTemplate, String.class, loqateKey, addressQuery);
            return response != null && !response.contains("\"Error\":") && response.contains("\"Type\":\"Address\"");
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        return true;
    }
}
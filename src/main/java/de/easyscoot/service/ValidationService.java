package de.easyscoot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidationService {

    private final String apiKey = "f1fa5825285148d2904c6c242a293ad3";
    private final String baseUrl = "https://emailreputation.abstractapi.com/v1/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String loqateKey = "UA99-XW38-HU72-BX52";
    private final String loqateUrl = "https://api.addressy.com/Capture/Interactive/Find/v1.10/json3.ws";

    public boolean isValid(String email) {
        String url = baseUrl + "?api_key=" + apiKey + "&email=" + email;

        try {
            String response = restTemplate.getForObject(url, String.class);
            return response != null && response.contains("\"deliverable\"");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidAdresse(String street, Integer streetNumber, Integer plz, String location) {
        String urlTemplate = loqateUrl + "?Key={key}&Countries=DE&Text={text}";
        String addressQuery = street + " " + streetNumber + ", " + plz + " " + location;

        try {
            String response = restTemplate.getForObject(urlTemplate, String.class, loqateKey, addressQuery);

            return response != null && !response.contains("\"Error\":") && response.contains("\"Type\":\"Address\"");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return true;
        }
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // 4. Проверяем наличие хотя бы одной цифры
        if (!password.matches(".*[0-9].*")) {
            return false;
        }

        return true;
    }
}
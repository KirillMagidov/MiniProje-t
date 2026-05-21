package de.easyscoot.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.easyscoot.model.EScooter;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScooterRepository implements IScooterRepository {

    private final Gson gson = new Gson();
    String filePath = "Scooter.json";

    @Override
    public List<EScooter> findAll() {

        try (FileReader reader = new FileReader(filePath)) {

            Type listType = new TypeToken<List<EScooter>>() {
            }.getType();
            List<EScooter> scooters = gson.fromJson(reader, listType);

            // Return scooter oder leere list
            return scooters != null ? scooters : new ArrayList<>(); //return scooter oder leere list

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Lesen der Datei: " + filePath, e);
        }
    }


    @Override
    public EScooter findById(String id) {
        // Alle Scooter laden
        List<EScooter> scooters = findAll();

        //Scooter in Liste suchen
        for (EScooter scooter : scooters) {
            if (scooter.getId().equals(id)) {
                return scooter;
            }
        }

        // Wenn keiner gefunden wurde
        throw new RuntimeException("Kein EScooter mit der ID '" + id + "' gefunden");
    }


    @Override
    public void save(EScooter scooterToAdd) {

        if (scooterToAdd == null) {
            throw new IllegalArgumentException("Scooter darf nicht null sein");
        }

        try {
            // Alle Scooter laden
            List<EScooter> scooters = findAll();

            // Scooter hinzufügen
            scooters.add(scooterToAdd);

            // Liste zurückschreiben
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(scooters, writer);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Speichern des Scooters in " + filePath, e);
        }
    }

    @Override
    public void delete(String id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID darf nicht null oder leer sein");
        }

        String filePath = "Scooter.json";

        try {
            // Prüfe ob Scooter existiert
            EScooter scooterToDelete = findById(id);

            // Alle Scooter laden
            List<EScooter> scooters = findAll();

            // 3. SCooter entfernen
            scooters.remove(scooterToDelete);

            // 4. Liste zurückschreiben
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(scooters, writer);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Löschen des Scooters mit ID: " + id, e);
        }
    }
}

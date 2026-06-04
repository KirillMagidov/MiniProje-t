package de.easyscoot.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.easyscoot.model.EScooter;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScooterRepository implements IScooterRepository {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath = "src/main/java/de/easyscoot/Database/Scooter.json";

    @Override
    public List<EScooter> findAll() {

        try (FileReader reader = new FileReader(filePath)) {

            Type listType = new TypeToken<List<EScooter>>() {
            }.getType();
            List<EScooter> scooters = gson.fromJson(reader, listType);

            // Return scooter oder leere list
            return scooters != null ? scooters : new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Lesen der Datei: " + filePath, e);
        }
    }


    @Override
    public EScooter findById(String id) {
        List<EScooter> scooters = findAll();
        for (EScooter scooter : scooters) {
            if (scooter.getId() != null && scooter.getId().equals(id)) {
                return scooter;
            }
        }
        return null;
    }


    @Override
    public void save(EScooter scooterToAdd) {

        if (scooterToAdd == null) {
            throw new IllegalArgumentException("Scooter darf nicht null sein");
        }

        try {
            // Alle Scooter laden
            List<EScooter> scooters = findAll();
            boolean isUpdated = false;

            // berpruefen, ob der Scooter bereits existiert (Update)
            for (int i = 0; i < scooters.size(); i++) {
                String existingId = scooters.get(i).getId();
                if (existingId != null && existingId.equals(scooterToAdd.getId())) {
                    scooters.set(i, scooterToAdd);
                    isUpdated = true;
                    break;
                }
            }

            // Wenn er nicht existiert, neu hinzufuegen (Insert)
            if (!isUpdated) {
                scooters.add(scooterToAdd);
            }

            // Liste zurueckschreiben
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

        try {
            List<EScooter> scooters = findAll();

            // Loescht Scooter
            boolean removed = scooters.removeIf(s -> s.getId().equals(id));

            // Falls kein Scooter gefunden
            if (!removed) {
                throw new RuntimeException("Kein Scooter mit der ID " + id + " gefunden");
            }

            // Liste zurueckschreiben
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(scooters, writer);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Loeschen des Scooters mit ID: " + id, e);
        }
    }
}
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.easyscoot.model.*;
import de.easyscoot.repository.ScooterRepository;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScooterRepositoryTest {

    private ScooterRepository repository;
    private Path tempFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BeforeEach
    void setUp() throws Exception {

        // Temporäre Datei erzeugen
        tempFile = Files.createTempFile("scooter_test", ".json");

        // Eigene Testdaten erzeugen
        List<EScooter> testData = List.of(
                new EScooter(
                        Maintenancestatus.NOT_IN_WARTUNG,
                        "S1",
                        "Voi",
                        "ES2",
                        52.1,
                        9.7,
                        80,
                        12000,
                        20,
                        Drivestatus.STANDING,
                        Availability.NICHT_IN_BENUTZUNG
                ),
                new EScooter(
                        Maintenancestatus.IN_WARTUNG,
                        "S2",
                        "Tier",
                        "Ninebot Max",
                        52.2,
                        9.8,
                        60,
                        13000,
                        18,
                        Drivestatus.DRIVING,
                        Availability.IN_BENUTZUNG
                )
        );

        // Testdaten in Datei schreiben
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            gson.toJson(testData, writer);
        }

        // Repository erzeugen
        repository = new ScooterRepository();

        // filePath per Reflection überschreiben
        Field filePathField = ScooterRepository.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(repository, tempFile.toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }




    @Test
    void testSave_insertNew() {
        EScooter newScooter = new EScooter(
                Maintenancestatus.NOT_IN_WARTUNG,
                "S3",
                "Xiaomi",
                "KQi3 Pro",
                52.3,
                9.9,
                90,
                14000,
                22,
                Drivestatus.STANDING,
                Availability.IN_BENUTZUNG
        );

        repository.save(newScooter);

        EScooter loaded = repository.findById("S3");
        assertNotNull(loaded);
        assertEquals("Xiaomi", loaded.getMarke());
    }

    @Test
    void testSave_updateExisting() {
        EScooter updated = new EScooter(
                Maintenancestatus.NOT_IN_WARTUNG,
                "S1",
                "Voi",
                "ES2 UPDATED",
                52.15,
                9.75,
                50,
                12000,
                20,
                Drivestatus.DRIVING,
                Availability.IN_BENUTZUNG
        );

        repository.save(updated);

        EScooter scooter = repository.findById("S1");
        assertEquals("ES2 UPDATED", scooter.getModell());
        assertEquals(50, scooter.getLadezustand());
        assertEquals(Drivestatus.DRIVING, scooter.getDrivestatus());
    }



    @Test
    void testDelete() {
        assertThrows(RuntimeException.class,
                () -> repository.delete("UNKNOWN"));
    }
}

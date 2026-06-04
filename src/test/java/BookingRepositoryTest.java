
import de.easyscoot.model.Booking;
import de.easyscoot.repository.BookingRepository;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingRepositoryTest {

    private BookingRepository repository;
    private Path tempFile;

    @BeforeEach
    void setUp() throws Exception {

        // Temporäre Datei erzeugen
        tempFile = Files.createTempFile("booking_test", ".json");

        // Testdaten erzeugen
        List<Booking> testData = List.of(
                new Booking(
                        "B1",
                        LocalTime.of(12, 0),
                        LocalTime.of(12, 30),
                        LocalDate.of(2026, 6, 1),
                        2.50,
                        "CUST1",
                        "SCOOTER1"
                ),
                new Booking(
                        "B2",
                        LocalTime.of(13, 0),
                        LocalTime.of(13, 45),
                        LocalDate.of(2026, 6, 2),
                        3.00,
                        "CUST2",
                        "SCOOTER2"
                )
        );

        // Testdaten in die Datei schreiben
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write(new com.google.gson.GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalTime.class,
                            (com.google.gson.JsonSerializer<LocalTime>) (src, type, ctx) ->
                                    ctx.serialize(src.toString()))
                    .registerTypeAdapter(LocalTime.class,
                            (com.google.gson.JsonDeserializer<LocalTime>) (json, type, ctx) ->
                                    LocalTime.parse(json.getAsString()))
                    .registerTypeAdapter(LocalDate.class,
                            (com.google.gson.JsonSerializer<LocalDate>) (src, type, ctx) ->
                                    ctx.serialize(src.toString()))
                    .registerTypeAdapter(LocalDate.class,
                            (com.google.gson.JsonDeserializer<LocalDate>) (json, type, ctx) ->
                                    LocalDate.parse(json.getAsString()))
                    .create()
                    .toJson(testData));
        }

        // Repository erzeugen
        repository = new BookingRepository();

        // filePath per Reflection überschreiben
        Field filePathField = BookingRepository.class.getDeclaredField("filePath");
        filePathField.setAccessible(true);
        filePathField.set(repository, tempFile.toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }


    @Test
    void testFindBookingByID_notExisting() {
        assertThrows(RuntimeException.class,
                () -> repository.findBookingByID("UNKNOWN"));
    }

    @Test
    void testSaveBookingEntry() {
        Booking newBooking = new Booking(
                "B3",
                LocalTime.of(14, 0),
                LocalTime.of(14, 20),
                LocalDate.of(2026, 6, 3),
                1.50,
                "CUST3",
                "SCOOTER3"
        );

        repository.saveBookingEntry(newBooking);

        Booking loaded = repository.findBookingByID("B3");
        assertNotNull(loaded);
        assertEquals(LocalTime.of(14, 0), loaded.getStartingTime());
        assertEquals(LocalDate.of(2026, 6, 3), loaded.getBookingDate());
    }
}

package de.easyscoot.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import de.easyscoot.model.Booking;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepository implements IBookingRepository{

    private final String filePath = "src/main/java/de/easyscoot/Database/Booking.json";
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalTime.class,
                    (JsonSerializer<LocalTime>) (src, type, ctx) ->
                            ctx.serialize(src.format(DateTimeFormatter.ISO_LOCAL_TIME)))
            .registerTypeAdapter(LocalTime.class,
                    (JsonDeserializer<LocalTime>) (json, type, ctx) ->
                            LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME))
            .registerTypeAdapter(LocalDate.class,
                    (JsonSerializer<LocalDate>) (src, type, ctx) ->
                            ctx.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class,
                    (JsonDeserializer<LocalDate>) (json, type, ctx) ->
                            LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();
    // trägt eine Buchung eines E-Scooters ein
    @Override
    public void saveBookingEntry(Booking booking) {
        List<Booking> bookings = getAllBookings();
        bookings.add(booking);

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(bookings, writer); //schreibt Buchung in die Datei
        } catch (Exception e) {
            e.printStackTrace(); //Falls fehler auftritt wird es angezeigt
        }
    }

    // löscht eine Buchung nach Beenden der Fahrt
    @Override
    public void deleteBookingEntry(String bookingID) {
        String filePath = "src/main/java/de/easyscoot/Database/Booking.json";

        try {
            // Prüfe ob Buchung existiert
            Booking bookingToDelete = findBookingByID(bookingID);

            // Alle Buchungen laden
            List<Booking> bookings = getAllBookings();

            // 3. Buchung entfernen
            bookings.removeIf(b -> b.getBookingID().equals(bookingToDelete.getBookingID()));
            // 4. Liste zurückschreiben
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(bookings, writer);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Löschen der Buchung mit der ID: " + bookingID, e);
        }
    }

    // gibt Liste mit allen gebuchten E-Scootern zurück
    @Override
    public List<Booking> getAllBookings() {

        try (FileReader reader = new FileReader(filePath)) {

            Type listType = new TypeToken<List<Booking>>() {
            }.getType();
            List<Booking> bookings = gson.fromJson(reader, listType);

            // Return scooter oder leere list
            return bookings != null ? bookings : new ArrayList<>(); //return scooter oder leere list

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Lesen der Datei: " + filePath, e);
        }
    }

    @Override
    public Booking findBookingByID(String bookingID) {
        List<Booking> bookings = getAllBookings();

        //Buchung in Liste suchen
        for (Booking booking : bookings) {
            if (booking.getBookingID().equals(bookingID)) {
                return booking;
            }
        }

        // Wenn keiner gefunden wurde
        throw new RuntimeException("Keine Buchung mit der ID '" + bookingID + "' gefunden");
    }
}

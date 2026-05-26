package de.easyscoot.service;

import de.easyscoot.model.*;
import de.easyscoot.repository.IScooterRepository;
import de.easyscoot.repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


public class CustomerService implements ICustomerService{


    private IScooterRepository scooterRepository;
    private BookingRepository bookingRepository;


    @Override
    public void bookEScooter(Customer customer, EScooter chosenEScooter) {
        chosenEScooter = searchEScooter();
        if  (chosenEScooter == null) {
            throw new RuntimeException("Kein E-Scooter verfügbar");
        }
        // Ermittlung der fehlenden Daten für das Starten der Fahrt
        String bookingID = UUID.randomUUID().toString();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentStartingTime = LocalTime.now();
        // generieren einr neuen Buchung
        Booking booking = new Booking(
               bookingID,
               currentStartingTime,
               null,
                currentDate,
                0,
                null,
                customer,
                chosenEScooter);
        bookingRepository.saveBookingEntry(booking);
    }
    // anbindung an das Frontend mit Übersicht vom Fahrtende, Kunde klickt auf Fahrt beenden
    @Override
    public void stopEScooter(Booking booking) {

        if (booking == null) {
            throw new RuntimeException("Keine Buchung vorhanden");
        }

        // Berechung der letzten Fehlern werte für die Zusammenfeasende Übersicht

        LocalTime currentEndingTime = LocalTime.now();
        Integer currentBookingPrice = booking.getBookingPrice();
        Long currentBookingDuration = booking.getBookingDuration(booking.getStartingTime(), currentEndingTime);

        // E-Scooter wieder verfügbar machen

        EScooter stopedEscooter = booking.getEScooter();
        stopedEscooter.setAvailability(Availability.NOT_IN_USE);
        stopedEscooter.setDrivestatus(Drivestatus.STANDING);

        // buchung aus dem REpo löschen

        bookingRepository.deleteBookingEntry(booking.getBookingID());
    }

    @Override
    public EScooter searchEScooter() {
        List<EScooter> escooters = scooterRepository.findAll();
        for (EScooter escooter : escooters) {                                // der Kunde wählt den E-Scooter
            if(escooter.getAvailability() == Availability.NOT_IN_USE && escooter.getDrivestatus() == Drivestatus.STANDING && escooter.getStatus() == Maintenancestatus.NOT_IN_WARTUNG) {
                escooter.setAvailability(Availability.IN_USE);
                escooter.setDrivestatus(Drivestatus.DRIVING);
                return escooter;
            }
        }
        throw new RuntimeException("No Scooter available");
    }
}

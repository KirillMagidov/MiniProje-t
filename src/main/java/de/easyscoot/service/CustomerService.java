package de.easyscoot.service;

import de.easyscoot.model.*;
import de.easyscoot.repository.IScooterRepository;
import de.easyscoot.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService implements ICustomerService {

    private final IScooterRepository scooterRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public CustomerService(IScooterRepository scooterRepository, BookingRepository bookingRepository) {
        this.scooterRepository = scooterRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void bookEScooter(Customer customer, EScooter chosenEScooter) {
        chosenEScooter = searchEScooter();

        String bookingID = UUID.randomUUID().toString();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentStartingTime = LocalTime.now();

        Booking booking = new Booking(
                bookingID,
                currentStartingTime,
                null,
                currentDate,
                0.0,
                customer.getCustomerId(),
                chosenEScooter.getId()
        );

        bookingRepository.saveBookingEntry(booking);
    }

    @Override
    public void stopEScooter(Booking booking) {
        if (booking == null) {
            throw new RuntimeException("Keine Buchung vorhanden");
        }

        LocalTime currentEndingTime = LocalTime.now();
        booking.setEndingTime(currentEndingTime);
        booking.setBookingPrice(booking.getBookingPrice());

        EScooter stoppedScooter = scooterRepository.findById(booking.getScooterId());
        stoppedScooter.setAvailability(Availability.NICHT_IN_BENUTZUNG);
        stoppedScooter.setDrivestatus(Drivestatus.STANDING);
        scooterRepository.save(stoppedScooter);

        bookingRepository.deleteBookingEntry(booking.getBookingID());
    }

    @Override
    public EScooter searchEScooter() {
        List<EScooter> escooters = scooterRepository.findAll();
        for (EScooter escooter : escooters) {
            if (escooter.getAvailability() == Availability.NICHT_IN_BENUTZUNG
                    && escooter.getDrivestatus() == Drivestatus.STANDING
                    && escooter.getStatus() == Maintenancestatus.NOT_IN_WARTUNG) {
                escooter.setAvailability(Availability.IN_BENUTZUNG);
                escooter.setDrivestatus(Drivestatus.DRIVING);
                return escooter;
            }
        }
        throw new RuntimeException("Kein Scooter verfügbar");
    }
}
package de.easyscoot.service;

import de.easyscoot.model.Availability;
import de.easyscoot.model.Booking;
import de.easyscoot.model.Customer;
import de.easyscoot.model.EScooter;
import de.easyscoot.repository.BookingRepository;
import de.easyscoot.repository.CustomerRepository;
import de.easyscoot.repository.ScooterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ScooterRepository scooterRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ScooterRepository scooterRepository, CustomerRepository customerRepository) {
        this.bookingRepository = bookingRepository;
        this.scooterRepository = scooterRepository;
        this.customerRepository = customerRepository;
    }

    public Booking startRide(String customerId, String scooterID) {
        EScooter scooter = scooterRepository.findById(scooterID);

        if (scooter.getAvailability() != Availability.NICHT_IN_BENUTZUNG) {
            throw new RuntimeException("Scooter ist schon gebucht oder nicht verfügbar");
        }

        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new RuntimeException("Kunde nicht gefunden");
        }

        scooter.setAvailability(Availability.IN_BENUTZUNG);
        scooterRepository.save(scooter);


        Booking newBooking = new Booking();

        //ID für Booking
        newBooking.setBookingID(UUID.randomUUID().toString().substring(0, 8));

        //Kunde drin
        newBooking.setCustomer(customer);
        newBooking.setEscooter(scooter);

        //Zeit und Data
        newBooking.setStartingTime(LocalTime.now());
        newBooking.setBookingDate(LocalDate.now());

        //Speichern
        bookingRepository.saveBookingEntry(newBooking);
        return newBooking;
    }

    public Booking endRide(String bookingID) {
        Booking booking = bookingRepository.findBookingByID(bookingID);
        EScooter scooter = booking.getEScooter();
        Customer customer = booking.getCustomer();

        if (scooter.getAvailability() != Availability.IN_BENUTZUNG) {
            throw new RuntimeException("Scooter ist nicht gebucht");
        }

        scooter.setAvailability(Availability.NICHT_IN_BENUTZUNG);
        scooterRepository.save(scooter);

        bookingRepository.deleteBookingEntry(bookingID);

        booking.setEndingTime(LocalTime.now());
        booking.setBookingDate(LocalDate.now());

        bookingRepository.saveBookingEntry(booking);

        return booking;
    }

    public List<Booking> getCustomerHistory(String customerID) {
        List<Booking> allBokings = bookingRepository.getAllBookings();
        List<Booking> customerHistory = new ArrayList<>();
        for (Booking booking : allBokings) {
            if(booking.getCustomer() != null && booking.getCustomer().getCustomerId().equals(customerID)) {
                customerHistory.add(booking);
            }
        }
        return customerHistory;
    }
}

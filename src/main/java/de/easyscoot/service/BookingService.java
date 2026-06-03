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
import java.time.Duration;
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
    private final CustomerService customerService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ScooterRepository scooterRepository, CustomerRepository customerRepository, CustomerService customerService) {
        this.bookingRepository = bookingRepository;
        this.scooterRepository = scooterRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
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

        // Überprüfen, ob genug Geld für die Fahrt vorhanden ist
        customerService.enoughMoneyForARide(customer);

        scooter.setAvailability(Availability.IN_BENUTZUNG);
        scooterRepository.save(scooter);

        Booking newBooking = new Booking();
        newBooking.setBookingID(UUID.randomUUID().toString().substring(0, 8));
        newBooking.setCustomerId(customerId);
        newBooking.setScooterId(scooterID);
        newBooking.setStartingTime(LocalTime.now());
        newBooking.setBookingDate(LocalDate.now());

        bookingRepository.saveBookingEntry(newBooking);
        return newBooking;
    }

    public Booking endRide(String bookingID) {
        Booking booking = bookingRepository.findBookingByID(bookingID);
        EScooter scooter = scooterRepository.findById(booking.getScooterId());
        Customer customer = customerRepository.getCustomerById(booking.getCustomerId());

        // Scooter freigeben
        scooter.setAvailability(Availability.NICHT_IN_BENUTZUNG);
        scooterRepository.save(scooter);

        bookingRepository.deleteBookingEntry(bookingID);
        booking.setEndingTime(LocalTime.now());
        long minutesDriven = Duration.between(booking.getStartingTime(), booking.getEndingTime()).toMinutes();
        double finalPrice = 2.00 + (minutesDriven * 0.15);
        booking.setBookingPrice(finalPrice);

        customerService.debitMoney(customer, finalPrice);

        bookingRepository.saveBookingEntry(booking);
        return booking;
    }

    public List<Booking> getCustomerHistory(String customerID) {
        List<Booking> allBookings = bookingRepository.getAllBookings();
        List<Booking> customerHistory = new ArrayList<>();
        for (Booking booking : allBookings) {
            if (booking.getCustomerId() != null && booking.getCustomerId().equals(customerID)) {
                customerHistory.add(booking);
            }
        }
        return customerHistory;
    }
}
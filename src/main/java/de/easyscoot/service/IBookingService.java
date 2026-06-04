package de.easyscoot.service;

import de.easyscoot.model.Booking;

import java.util.List;

public interface IBookingService {
    Booking startRide(String customerId, String scooterID);

    Booking endRide(String bookingID);

    List<Booking> getCustomerHistory(String customerID);
}

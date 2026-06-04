package de.easyscoot.service;

import de.easyscoot.model.Booking;

import java.util.List;

public interface IBookingService {
    public Booking startRide(String customerId, String scooterID);

    public Booking endRide(String bookingID);

    public List<Booking> getCustomerHistory(String customerID);
}

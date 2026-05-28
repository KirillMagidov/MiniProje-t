package de.easyscoot.repository;

import de.easyscoot.model.Booking;

import java.util.List;

public interface IBookingRepository {

    void saveBookingEntry(Booking booking);

    void deleteBookingEntry(String bookingID);

    List<Booking> getAllBookings();

    Booking findBookingByID(String bookingID);
}

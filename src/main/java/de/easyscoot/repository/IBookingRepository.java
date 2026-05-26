package de.easyscoot.repository;

import de.easyscoot.model.Booking;

import java.util.List;

public interface IBookingRepository {

    public void saveBookingEntry(Booking booking);

    public void deleteBookingEntry(String bookingID);

    public List<Booking> getAllBookings();

    public Booking findBookingByID(String bookingID);
}

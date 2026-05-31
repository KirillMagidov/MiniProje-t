package de.easyscoot.model;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private String bookingID;
    private LocalTime startingTime;
    private LocalTime endingTime;
    private LocalDate bookingDate;
    private Double bookingPrice;
    private String customerId;
    private String scooterId;

    public Booking() {}

    public Booking(String bookingID, LocalTime startingTime, LocalTime endingTime,
                   LocalDate bookingDate, Double bookingPrice,
                   String customerId, String scooterId) {
        this.bookingID = bookingID;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
        this.customerId = customerId;
        this.scooterId = scooterId;
    }

    public String getBookingID() { return bookingID; }
    public LocalTime getStartingTime() { return startingTime; }
    public LocalTime getEndingTime() { return endingTime; }
    public LocalDate getBookingDate() { return bookingDate; }
    public String getCustomerId() { return customerId; }
    public String getScooterId() { return scooterId; }

    public Double getBookingPrice() {
        if (startingTime == null || endingTime == null) return 0.0;
        long minutes = Duration.between(startingTime, endingTime).toMinutes();
        return Math.max(minutes * 0.15, 1.00);
    }

    public Long getBookingDuration() {
        if (startingTime == null || endingTime == null) return 0L;
        return Duration.between(startingTime, endingTime).toMinutes();
    }

    public void setBookingID(String bookingID) { this.bookingID = bookingID; }
    public void setStartingTime(LocalTime startingTime) { this.startingTime = startingTime; }
    public void setEndingTime(LocalTime endingTime) { this.endingTime = endingTime; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public void setBookingPrice(Double bookingPrice) { this.bookingPrice = bookingPrice; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setScooterId(String scooterId) { this.scooterId = scooterId; }
}
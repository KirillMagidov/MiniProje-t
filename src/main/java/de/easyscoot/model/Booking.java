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
    private Integer bookingPrice;
    private Time bookingDuration;
    private Customer customer;
    private EScooter escooter;

    public Booking(String bookingID, LocalTime startingTime, LocalTime endingTime, LocalDate bookingDate, Integer bookingPrice,
                   Time bookingDuration, Customer customer, EScooter escooter) {
        this.bookingID = bookingID;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
        this.bookingDuration = bookingDuration;
        this.customer = customer;
        this.escooter = escooter;
    }

    public String getBookingID() {
        return bookingID;
    }
    public LocalTime getStartingTime() {
        return startingTime;
    }
    public LocalTime getEndingTime() {
        return endingTime;
    }
    public LocalDate getBookingDate() {
        return bookingDate;
    }
    public Integer getBookingPrice() {
        return bookingPrice;
    }
    public Long getBookingDuration(LocalTime startingTime, LocalTime endingTime) {
        return Duration.between(startingTime, endingTime).toMinutes();
    }
    public Customer getCustomer() {
        return customer;
    }
    public EScooter getEScooter() {
        return escooter;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }
    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
    public void setBookingPrice(Integer bookingPrice) {
        this.bookingPrice = bookingPrice;
    }
    public void setBookingDuration(Integer bookingDuration) {
        this.bookingDuration = bookingDuration;
    }
    public void setCustomer(String customerID) {
        this.customer = customer;
    }
    public void seteScooter(String eScooterID) {
        this.escooter = escooter;
    }
}

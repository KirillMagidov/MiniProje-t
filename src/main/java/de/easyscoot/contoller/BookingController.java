package de.easyscoot.contoller;

import de.easyscoot.model.Booking;
import de.easyscoot.model.StartRideRequest;
import de.easyscoot.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking/start")
    public Booking startRide(@RequestBody StartRideRequest request) {
        return bookingService.startRide(request.getCustomerId(), request.getScooterId());
    }

    @PostMapping("/booking/end")
    public Booking endRide(@RequestParam("bookingId") String bookingId) {
        return bookingService.endRide(bookingId);
    }

    @GetMapping("/bookings/history")
    public List<Booking> getHistory(@RequestParam("customerId") String customerId) {
        return bookingService.getCustomerHistory(customerId);
    }
}

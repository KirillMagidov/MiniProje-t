package de.easyscoot.contoller;

import de.easyscoot.model.Booking;
import de.easyscoot.repository.BookingRepository;
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
    public Booking startRide(@RequestBody String customerId, @RequestParam String scooterId) {
        return bookingService.startRide(customerId, scooterId);
    }

    @PostMapping("/booking/end")
    public Booking endRide(@RequestBody String bookingId) {
        return bookingService.endRide(bookingId);
    }

    @GetMapping("/bookings/history")
    public List<Booking> getHistory(@RequestParam String customerId) {
        return bookingService.getCustomerHistory(customerId);
    }
}

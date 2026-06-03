package de.easyscoot.contoller;

import de.easyscoot.model.Booking;
import de.easyscoot.model.StartRideRequest;
import de.easyscoot.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> startRide(@RequestBody StartRideRequest request) {
        try {
            Booking booking = bookingService.startRide(request.getCustomerId(), request.getScooterId());
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/booking/end")
    public ResponseEntity<?> endRide(@RequestParam("bookingId") String bookingId) {
        try {
            Booking booking = bookingService.endRide(bookingId);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/bookings/history")
    public List<Booking> getHistory(@RequestParam("customerId") String customerId) {
        return bookingService.getCustomerHistory(customerId);
    }
}
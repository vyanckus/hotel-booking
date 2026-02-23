package com.example.hotel_booking.web.controller;

import com.example.hotel_booking.mapper.BookingMapper;
import com.example.hotel_booking.service.BookingService;
import com.example.hotel_booking.service.RoomService;
import com.example.hotel_booking.service.UserService;
import com.example.hotel_booking.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking.web.model.response.BookingListResponse;
import com.example.hotel_booking.web.model.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final RoomService roomService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookingListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        var bookingPage = bookingService.findAll(pageable);

        var response = new BookingListResponse();
        response.setBookings(bookingMapper.bookingListToResponseList(bookingPage.getContent()));
        response.setTotalElements(bookingPage.getTotalElements());
        response.setTotalPages(bookingPage.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long id) {
        var booking = bookingService.findById(id);
        return ResponseEntity.ok(bookingMapper.bookingToResponse(booking));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid UpsertBookingRequest request) {
        var booking = bookingService.createBooking(
                request.getRoomId(),
                request.getUserId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingMapper.bookingToResponse(booking));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

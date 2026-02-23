package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BookingService {
    Booking createBooking(Long roomId, Long userId, LocalDate checkIn, LocalDate checkOut);
    Page<Booking> findAll(Pageable pageable);
    Booking findById(Long id);
    void deleteById(Long id);
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}

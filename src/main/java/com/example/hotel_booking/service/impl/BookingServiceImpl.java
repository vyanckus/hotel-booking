package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.Booking;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.kafka.event.BookingEvent;
import com.example.hotel_booking.kafka.producer.StatisticsEventProducer;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.service.BookingService;
import com.example.hotel_booking.service.RoomService;
import com.example.hotel_booking.service.UserService;
import com.example.hotel_booking.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;
    private final StatisticsEventProducer statisticsEventProducer;

    @Override
    @Transactional
    public Booking createBooking(Long roomId, Long userId, LocalDate checkIn, LocalDate checkOut) {
        Room room = roomService.findById(roomId);
        User user = userService.findById(userId);

        if (!isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new IllegalArgumentException("Комната недоступна на указанные даты");
        }

        Booking booking = Booking.builder()
                .room(room)
                .user(user)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        BookingEvent event = new BookingEvent(
                savedBooking.getUser().getId(),
                savedBooking.getRoom().getId(),
                savedBooking.getRoom().getHotel().getId(),
                savedBooking.getCheckInDate(),
                savedBooking.getCheckOutDate(),
                Instant.now()
        );
        statisticsEventProducer.sendBookingEvent(event);

        return savedBooking;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с ID " + id + " не найдено"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Booking booking = findById(id);
        bookingRepository.delete(booking);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                roomId, checkIn, checkOut);
        return conflictingBookings.isEmpty();
    }
}

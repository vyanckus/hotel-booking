package com.example.hotel_booking.mapper;

import com.example.hotel_booking.entity.Booking;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking.web.model.response.BookingResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public Booking requestToBooking(UpsertBookingRequest request, Room room, User user) {
        if (request == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setRoom(room);
        booking.setUser(user);

        return booking;
    }

    public BookingResponse bookingToResponse(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());

        if (booking.getRoom() != null) {
            response.setRoomId(booking.getRoom().getId());
            response.setRoomName(booking.getRoom().getName());
        }

        if (booking.getUser() != null) {
            response.setUserId(booking.getUser().getId());
            response.setUserName(booking.getUser().getUsername());
        }

        return response;
    }

    public List<BookingResponse> bookingListToResponseList(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        return bookings.stream()
                .map(this::bookingToResponse)
                .collect(Collectors.toList());
    }
}

package com.example.hotel_booking.mapper;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking.web.model.response.HotelResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public Hotel requestToHotel(UpsertHotelRequest request) {
        if (request == null) {
            return null;
        }

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setTitle(request.getTitle());
        hotel.setCity(request.getCity());
        hotel.setAddress(request.getAddress());
        hotel.setDistanceFromCenter(request.getDistanceFromCenter());

        return hotel;
    }

    public Hotel requestToHotel(Long id, UpsertHotelRequest request) {
        Hotel hotel = requestToHotel(request);
        hotel.setId(id);
        return hotel;
    }

    public HotelResponse hotelToResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setTitle(hotel.getTitle());
        response.setCity(hotel.getCity());
        response.setAddress(hotel.getAddress());
        response.setDistanceFromCenter(hotel.getDistanceFromCenter());
        response.setRating(hotel.getRating());
        response.setNumberOfRatings(hotel.getNumberOfRatings());

        return response;
    }

    public List<HotelResponse> hotelListToResponseList(List<Hotel> hotels) {
        if (hotels == null) {
            return null;
        }
        return hotels.stream()
                .map(this::hotelToResponse)
                .collect(Collectors.toList());
    }
}

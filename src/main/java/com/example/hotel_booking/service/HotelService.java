package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.web.model.filter.HotelFilter;
import com.example.hotel_booking.web.model.response.HotelListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService {
    Page<Hotel> findAll(Pageable pageable);
    Hotel findById(Long id);
    Hotel save(Hotel hotel);
    Hotel update(Hotel hotel);
    void deleteById(Long id);
    Hotel rateHotel(Long id, Integer rating);
    HotelListResponse filterBy(HotelFilter filter);
}

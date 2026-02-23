package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.web.model.filter.RoomFilter;
import com.example.hotel_booking.web.model.response.RoomListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    Page<Room> findAll(Pageable pageable);
    Room findById(Long id);
    Room save(Room room);
    Room update(Room room);
    void deleteById(Long id);
    RoomListResponse filterBy(RoomFilter filter);
}

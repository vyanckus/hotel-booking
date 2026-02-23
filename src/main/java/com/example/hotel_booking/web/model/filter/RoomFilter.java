package com.example.hotel_booking.web.model.filter;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RoomFilter {
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private Long id;
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Integer maxGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long hotelId;
}

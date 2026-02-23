package com.example.hotel_booking.web.model.filter;

import lombok.Data;

@Data
public class HotelFilter {
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private Double distanceFromCenter;
}

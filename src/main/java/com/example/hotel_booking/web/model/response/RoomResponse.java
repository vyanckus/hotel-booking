package com.example.hotel_booking.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private String roomNumber;
    private Double price;
    private Integer maxGuests;
    private Long hotelId;
    private String hotelName;
}

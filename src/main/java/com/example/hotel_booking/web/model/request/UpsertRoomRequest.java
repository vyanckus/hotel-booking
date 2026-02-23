package com.example.hotel_booking.web.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpsertRoomRequest {

    @NotBlank(message = "Название комнаты не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotBlank(message = "Номер комнаты не может быть пустым")
    private String roomNumber;

    @NotNull(message = "Цена не может быть пустой")
    @Positive(message = "Цена должна быть положительным числом")
    private Double price;

    @NotNull(message = "Количество гостей не может быть пустым")
    @Positive(message = "Количество гостей должно быть положительным числом")
    private Integer maxGuests;

    @NotNull(message = "ID отеля не может быть пустым")
    @Positive(message = "ID отеля должен быть положительным числом")
    private Long hotelId;
}

package com.example.hotel_booking.web.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpsertHotelRequest {

    @NotBlank(message = "Название отеля не может быть пустым")
    @Size(min = 2, max = 100, message = "Название отеля должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Заголовок объявления не может быть пустым")
    private String title;

    @NotBlank(message = "Город не может быть пустым")
    private String city;

    @NotBlank(message = "Адрес не может быть пустым")
    private String address;

    @NotNull(message = "Расстояние от центра не может быть пустым")
    @Positive(message = "Расстояние должно быть положительным числом")
    private Double distanceFromCenter;
}

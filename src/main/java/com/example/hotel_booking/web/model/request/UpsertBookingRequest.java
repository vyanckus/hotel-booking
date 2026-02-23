package com.example.hotel_booking.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UpsertBookingRequest {

    @NotNull(message = "Дата заезда не может быть пустой")
    private LocalDate checkInDate;

    @NotNull(message = "Дата выезда не может быть пустой")
    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDate checkOutDate;

    @NotNull(message = "ID комнаты не может быть пустым")
    private Long roomId;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;
}

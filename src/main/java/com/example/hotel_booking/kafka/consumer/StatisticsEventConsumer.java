package com.example.hotel_booking.kafka.consumer;

import com.example.hotel_booking.kafka.event.BookingEvent;
import com.example.hotel_booking.kafka.event.UserRegistrationEvent;
import com.example.hotel_booking.statistics.entity.StatisticsDocument;
import com.example.hotel_booking.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsEventConsumer {
    private final StatisticsRepository statisticsRepository;

    @KafkaListener(topics = "user-registration-topic", groupId = "hotel-group")
    public void handleUserRegistration(UserRegistrationEvent event) {
        log.info("Received user registration event: {}", event);

        StatisticsDocument doc = StatisticsDocument.builder()
                .eventType("REGISTRATION")
                .userId(event.getUserId())
                .username(event.getUsername())
                .email(event.getEmail())
                .timestamp(event.getTimestamp())
                .build();

        statisticsRepository.save(doc);
        log.info("Saved registration statistics for user: {}", event.getUserId());
    }

    @KafkaListener(topics = "booking-topic", groupId = "hotel-group")
    public void handleBooking(BookingEvent event) {
        log.info("Received booking event: {}", event);

        StatisticsDocument doc = StatisticsDocument.builder()
                .eventType("BOOKING")
                .userId(event.getUserId())
                .roomId(event.getRoomId())
                .hotelId(event.getHotelId())
                .checkInDate(event.getCheckInDate())
                .checkOutDate(event.getCheckOutDate())
                .timestamp(event.getTimestamp())
                .build();

        statisticsRepository.save(doc);
        log.info("Saved booking statistics for user: {}, room: {}", event.getUserId(), event.getRoomId());
    }
}

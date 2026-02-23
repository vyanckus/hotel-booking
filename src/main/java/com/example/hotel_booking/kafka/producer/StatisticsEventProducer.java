package com.example.hotel_booking.kafka.producer;

import com.example.hotel_booking.kafka.event.BookingEvent;
import com.example.hotel_booking.kafka.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistrationEvent(UserRegistrationEvent event) {
        kafkaTemplate.send("user-registration-topic", event);
        log.info("Sent user registration event for user: {}", event.getUserId());
    }

    public void sendBookingEvent(BookingEvent event) {
        kafkaTemplate.send("booking-topic", event);
        log.info("Sent booking event for room: {}, user: {}", event.getRoomId(), event.getUserId());
    }
}

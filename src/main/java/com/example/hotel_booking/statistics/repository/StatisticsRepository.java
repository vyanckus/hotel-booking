package com.example.hotel_booking.statistics.repository;

import com.example.hotel_booking.statistics.entity.StatisticsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends MongoRepository<StatisticsDocument, String> {
}

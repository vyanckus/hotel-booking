package com.example.hotel_booking.statistics.service;

import com.example.hotel_booking.statistics.entity.StatisticsDocument;
import com.example.hotel_booking.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public StatisticsDocument save(StatisticsDocument document) {
        return statisticsRepository.save(document);
    }

    public List<StatisticsDocument> findAll() {
        return statisticsRepository.findAll();
    }
}

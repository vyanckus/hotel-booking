package com.example.hotel_booking.statistics.service;

import com.example.hotel_booking.statistics.entity.StatisticsDocument;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvExportService {
    private final StatisticsService statisticsService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public void exportToCsv(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=statistics_" +
                        java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");

        List<StatisticsDocument> statistics = statisticsService.findAll();

        try (PrintWriter writer = response.getWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            String[] header = {"Event Type", "User ID", "Username", "Email", "Room ID",
                    "Hotel ID", "Check In Date", "Check Out Date", "Timestamp"};
            csvWriter.writeNext(header);

            for (StatisticsDocument doc : statistics) {
                String[] row = {
                        doc.getEventType(),
                        doc.getUserId() != null ? doc.getUserId().toString() : "",
                        doc.getUsername() != null ? doc.getUsername() : "",
                        doc.getEmail() != null ? doc.getEmail() : "",
                        doc.getRoomId() != null ? doc.getRoomId().toString() : "",
                        doc.getHotelId() != null ? doc.getHotelId().toString() : "",
                        doc.getCheckInDate() != null ? doc.getCheckInDate().format(DATE_FORMATTER) : "",
                        doc.getCheckOutDate() != null ? doc.getCheckOutDate().format(DATE_FORMATTER) : "",
                        doc.getTimestamp() != null ? doc.getTimestamp().toString() : ""
                };
                csvWriter.writeNext(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выгрузке CSV", e);
        }
    }
}

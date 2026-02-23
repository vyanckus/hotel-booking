package com.example.hotel_booking.web.controller;

import com.example.hotel_booking.statistics.service.CsvExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final CsvExportService csvExportService;

    @GetMapping("/export/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportCsv(HttpServletResponse response) {
        csvExportService.exportToCsv(response);
    }
}

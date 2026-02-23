package com.example.hotel_booking.web.controller;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.mapper.HotelMapper;
import com.example.hotel_booking.service.HotelService;
import com.example.hotel_booking.web.model.filter.HotelFilter;
import com.example.hotel_booking.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking.web.model.response.HotelListResponse;
import com.example.hotel_booking.web.model.response.HotelResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        var hotelPage = hotelService.findAll(pageable);

        var response = new HotelListResponse();
        response.setHotels(hotelMapper.hotelListToResponseList(hotelPage.getContent()));
        response.setTotalElements(hotelPage.getTotalElements());
        response.setTotalPages(hotelPage.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> findById(@PathVariable Long id) {
        var hotel = hotelService.findById(id);
        return ResponseEntity.ok(hotelMapper.hotelToResponse(hotel));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> create(@RequestBody @Valid UpsertHotelRequest request) {
        var hotel = hotelMapper.requestToHotel(request);
        var savedHotel = hotelService.save(hotel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.hotelToResponse(savedHotel));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid UpsertHotelRequest request) {
        var hotel = hotelMapper.requestToHotel(id, request);
        var updatedHotel = hotelService.update(hotel);

        return ResponseEntity.ok(hotelMapper.hotelToResponse(updatedHotel));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> rateHotel(
            @PathVariable Long id,
            @RequestParam @Min(1) @Max(5) Integer rating) {

        Hotel updatedHotel = hotelService.rateHotel(id, rating);
        return ResponseEntity.ok(hotelMapper.hotelToResponse(updatedHotel));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelListResponse> filterBy(HotelFilter filter) {
        return ResponseEntity.ok(hotelService.filterBy(filter));
    }
}

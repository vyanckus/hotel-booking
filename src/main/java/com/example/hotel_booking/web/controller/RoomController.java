package com.example.hotel_booking.web.controller;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.mapper.RoomMapper;
import com.example.hotel_booking.service.HotelService;
import com.example.hotel_booking.service.RoomService;
import com.example.hotel_booking.web.model.filter.RoomFilter;
import com.example.hotel_booking.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking.web.model.response.RoomListResponse;
import com.example.hotel_booking.web.model.response.RoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;
    private final RoomMapper roomMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        var roomPage = roomService.findAll(pageable);

        var response = new RoomListResponse();
        response.setRooms(roomMapper.roomListToResponseList(roomPage.getContent()));
        response.setTotalElements(roomPage.getTotalElements());
        response.setTotalPages(roomPage.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        var room = roomService.findById(id);
        return ResponseEntity.ok(roomMapper.roomToResponse(room));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        Room room = roomMapper.requestToRoom(request);
        room.setHotel(hotelService.findById(request.getHotelId()));

        var savedRoom = roomService.save(room);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.roomToResponse(savedRoom));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertRoomRequest request) {
        Room room = roomMapper.requestToRoom(id, request);
        if (request.getHotelId() != null) {
            room.setHotel(hotelService.findById(request.getHotelId()));
        }

        var updatedRoom = roomService.update(room);

        return ResponseEntity.ok(roomMapper.roomToResponse(updatedRoom));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomListResponse> filterBy(RoomFilter filter) {
        return ResponseEntity.ok(roomService.filterBy(filter));
    }
}

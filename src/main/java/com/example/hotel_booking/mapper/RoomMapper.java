package com.example.hotel_booking.mapper;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking.web.model.response.RoomResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public Room requestToRoom(UpsertRoomRequest request) {
        if (request == null) {
            return null;
        }

        Room room = new Room();
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setRoomNumber(request.getRoomNumber());
        room.setPrice(request.getPrice());
        room.setMaxGuests(request.getMaxGuests());

        return room;
    }

    public Room requestToRoom(Long id, UpsertRoomRequest request) {
        Room room = requestToRoom(request);
        room.setId(id);
        return room;
    }

    public RoomResponse roomToResponse(Room room) {
        if (room == null) {
            return null;
        }

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setDescription(room.getDescription());
        response.setRoomNumber(room.getRoomNumber());
        response.setPrice(room.getPrice());
        response.setMaxGuests(room.getMaxGuests());

        if (room.getHotel() != null) {
            response.setHotelId(room.getHotel().getId());
            response.setHotelName(room.getHotel().getName());
        }

        return response;
    }

    public List<RoomResponse> roomListToResponseList(List<Room> rooms) {
        if (rooms == null) {
            return null;
        }
        return rooms.stream()
                .map(this::roomToResponse)
                .collect(Collectors.toList());
    }
}

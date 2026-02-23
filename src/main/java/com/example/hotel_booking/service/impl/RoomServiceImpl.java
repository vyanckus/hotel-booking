package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.mapper.RoomMapper;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.service.HotelService;
import com.example.hotel_booking.service.RoomService;
import com.example.hotel_booking.specification.RoomSpecification;
import com.example.hotel_booking.utils.BeanUtils;
import com.example.hotel_booking.web.exception.EntityNotFoundException;
import com.example.hotel_booking.web.model.filter.RoomFilter;
import com.example.hotel_booking.web.model.response.RoomListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<Room> findAll(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комната с ID " + id + " не найдена"));
    }

    @Override
    @Transactional
    public Room save(Room room) {
        Hotel hotel = hotelService.findById(room.getHotel().getId());
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room update(Room room) {
        Room existedRoom = findById(room.getId());

        if (room.getHotel() != null && room.getHotel().getId() != null) {
            Hotel hotel = hotelService.findById(room.getHotel().getId());
            room.setHotel(hotel);
        }

        BeanUtils.copyNonNullProperties(room, existedRoom);
        return roomRepository.save(existedRoom);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomListResponse filterBy(RoomFilter filter) {
        Specification<Room> spec = RoomSpecification.withFilter(filter);
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());
        Page<Room> roomPage = roomRepository.findAll(spec, pageable);

        RoomListResponse response = new RoomListResponse();
        response.setRooms(roomMapper.roomListToResponseList(roomPage.getContent()));
        response.setTotalElements(roomPage.getTotalElements());
        response.setTotalPages(roomPage.getTotalPages());
        response.setCurrentPage(filter.getPageNumber());
        response.setPageSize(filter.getPageSize());

        return response;
    }
}

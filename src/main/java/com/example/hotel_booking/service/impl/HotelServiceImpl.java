package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.mapper.HotelMapper;
import com.example.hotel_booking.specification.HotelSpecification;
import com.example.hotel_booking.web.exception.EntityNotFoundException;
import com.example.hotel_booking.repository.HotelRepository;
import com.example.hotel_booking.service.HotelService;
import com.example.hotel_booking.utils.BeanUtils;
import com.example.hotel_booking.web.model.filter.HotelFilter;
import com.example.hotel_booking.web.model.response.HotelListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<Hotel> findAll(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отель с ID " + id + " не найден"));
    }

    @Override
    @Transactional
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public Hotel update(Hotel hotel) {
        Hotel existedHotel = findById(hotel.getId());
        BeanUtils.copyNonNullProperties(hotel, existedHotel);
        return hotelRepository.save(existedHotel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Hotel hotel = findById(id);
        hotelRepository.delete(hotel);
    }

    @Override
    @Transactional
    public Hotel rateHotel(Long id, Integer rating) {
        Hotel hotel = findById(id);

        int totalRating = (int) (hotel.getRating() * hotel.getNumberOfRatings());
        totalRating = totalRating - hotel.getRating().intValue() + rating;
        double newRating = (double) totalRating / (hotel.getNumberOfRatings() + 1);

        double roundedRating = Math.round(newRating * 10) / 10.0;

        hotel.setRating(roundedRating);
        hotel.setNumberOfRatings(hotel.getNumberOfRatings() + 1);

        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public HotelListResponse filterBy(HotelFilter filter) {
        Specification<Hotel> spec = HotelSpecification.withFilter(filter);
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());
        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);

        HotelListResponse response = new HotelListResponse();
        response.setHotels(hotelMapper.hotelListToResponseList(hotelPage.getContent()));
        response.setTotalElements(hotelPage.getTotalElements());
        response.setTotalPages(hotelPage.getTotalPages());
        response.setCurrentPage(filter.getPageNumber());
        response.setPageSize(filter.getPageSize());

        return response;
    }
}

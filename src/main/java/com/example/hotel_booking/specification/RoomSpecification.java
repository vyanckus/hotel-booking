package com.example.hotel_booking.specification;

import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.web.model.filter.RoomFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDate;

public class RoomSpecification {

    public static Specification<Room> withFilter(RoomFilter filter) {
        return byId(filter.getId())
                .and(byName(filter.getName()))
                .and(byPriceRange(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byMaxGuests(filter.getMaxGuests()))
                .and(byHotelId(filter.getHotelId()))
                .and(byAvailability(filter.getCheckInDate(), filter.getCheckOutDate()));
    }

    private static Specification<Room> byId(Long id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("id"), id);
        };
    }

    private static Specification<Room> byName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Room> byPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }

    private static Specification<Room> byMaxGuests(Integer maxGuests) {
        return (root, query, cb) -> {
            if (maxGuests == null) return null;
            return cb.greaterThanOrEqualTo(root.get("maxGuests"), maxGuests);
        };
    }

    private static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) return null;
            return cb.equal(root.get("hotel").get("id"), hotelId);
        };
    }

    private static Specification<Room> byAvailability(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, cb) -> {
            if (checkIn == null || checkOut == null) return null;

            var subquery = query.subquery(Long.class);
            var bookingRoot = subquery.from(com.example.hotel_booking.entity.Booking.class);
            subquery.select(bookingRoot.get("id"))
                    .where(cb.and(
                            cb.equal(bookingRoot.get("room").get("id"), root.get("id")),
                            cb.or(
                                    cb.between(bookingRoot.get("checkInDate"), checkIn, checkOut),
                                    cb.between(bookingRoot.get("checkOutDate"), checkIn, checkOut),
                                    cb.and(
                                            cb.lessThanOrEqualTo(bookingRoot.get("checkInDate"), checkIn),
                                            cb.greaterThanOrEqualTo(bookingRoot.get("checkOutDate"), checkOut)
                                    )
                            )
                    ));

            return cb.not(cb.exists(subquery));
        };
    }
}

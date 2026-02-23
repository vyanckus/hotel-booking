package com.example.hotel_booking.specification;

import com.example.hotel_booking.entity.Hotel;
import com.example.hotel_booking.web.model.filter.HotelFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class HotelSpecification {

    public static Specification<Hotel> withFilter(HotelFilter filter) {
        return byId(filter.getId())
                .and(byName(filter.getName()))
                .and(byTitle(filter.getTitle()))
                .and(byCity(filter.getCity()))
                .and(byAddress(filter.getAddress()))
                .and(byDistance(filter.getDistanceFromCenter()));
    }

    private static Specification<Hotel> byId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }
            return cb.equal(root.get("id"), id);
        };
    }

    private static Specification<Hotel> byName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> byTitle(String title) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(title)) {
                return null;
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> byCity(String city) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(city)) {
                return null;
            }
            return cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> byAddress(String address) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(address)) {
                return null;
            }
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> byDistance(Double distance) {
        return (root, query, cb) -> {
            if (distance == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("distanceFromCenter"), distance);
        };
    }
}

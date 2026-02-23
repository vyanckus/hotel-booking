package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    User findById(Long id);
    User save(User user);
    User update(User user);
    void deleteById(Long id);
    boolean existsByUsernameOrEmail(String username, String email);
    User findByUsername(String username);
}

package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.RoleType;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.kafka.event.UserRegistrationEvent;
import com.example.hotel_booking.kafka.producer.StatisticsEventProducer;
import com.example.hotel_booking.repository.UserRepository;
import com.example.hotel_booking.service.UserService;
import com.example.hotel_booking.utils.BeanUtils;
import com.example.hotel_booking.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatisticsEventProducer statisticsEventProducer;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(RoleType.ROLE_USER));
        }

        User savedUser = userRepository.save(user);

        UserRegistrationEvent event = new UserRegistrationEvent(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                Instant.now()
        );
        statisticsEventProducer.sendUserRegistrationEvent(event);

        return savedUser;
    }

    @Override
    @Transactional
    public User update(User user) {
        User existedUser = findById(user.getId());

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        BeanUtils.copyNonNullProperties(user, existedUser);
        return userRepository.save(existedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + username + " не найден"));
    }
}

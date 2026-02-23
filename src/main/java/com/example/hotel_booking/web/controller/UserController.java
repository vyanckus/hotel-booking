package com.example.hotel_booking.web.controller;

import com.example.hotel_booking.entity.RoleType;
import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.mapper.UserMapper;
import com.example.hotel_booking.service.UserService;
import com.example.hotel_booking.web.model.request.UpsertUserRequest;
import com.example.hotel_booking.web.model.response.UserListResponse;
import com.example.hotel_booking.web.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        var userPage = userService.findAll(pageable);

        var response = new UserListResponse();
        response.setUsers(userMapper.userListToResponseList(userPage.getContent()));
        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(userMapper.userToResponse(user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request) {
        if (userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким именем или email уже существует");
        }

        var user = userMapper.requestToUser(request);
        var savedUser = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(savedUser));
    }

    @PostMapping("/with-role")
    public ResponseEntity<UserResponse> createWithRole(
            @RequestBody @Valid UpsertUserRequest request,
            @RequestParam RoleType role) {

        if (userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким именем или email уже существует");
        }

        var user = userMapper.requestToUser(request);
        user.setRoles(Set.of(role));
        var savedUser = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertUserRequest request) {
        var user = userMapper.requestToUser(id, request);
        var updatedUser = userService.update(user);

        return ResponseEntity.ok(userMapper.userToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

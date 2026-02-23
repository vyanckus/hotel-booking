package com.example.hotel_booking.mapper;

import com.example.hotel_booking.entity.User;
import com.example.hotel_booking.web.model.request.UpsertUserRequest;
import com.example.hotel_booking.web.model.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User requestToUser(UpsertUserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public User requestToUser(Long id, UpsertUserRequest request) {
        User user = requestToUser(request);
        user.setId(id);
        return user;
    }

    public UserResponse userToResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }

    public List<UserResponse> userListToResponseList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::userToResponse)
                .collect(Collectors.toList());
    }
}

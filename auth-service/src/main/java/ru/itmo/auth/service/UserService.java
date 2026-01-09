package ru.itmo.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.auth.dto.UserResponse;
import ru.itmo.auth.entity.User;
import ru.itmo.auth.exception.UserNotFoundException;
import ru.itmo.auth.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(UUID id) {
        log.info("Get user by id: {}", id);
        User user ${DB_USER:***REMOVED***} userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public String getUserEmail(UUID id) {
        log.info("Get user email by id: {}", id);
        User user ${DB_USER:***REMOVED***} userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return user.getEmail();
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response ${DB_USER:***REMOVED***} new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}

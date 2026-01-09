package ru.itmo.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.auth.dto.UserResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // TODO: Implement user retrieval logic
    public UserResponse getUserById(UUID id) {
        log.info("Get user by id: {}", id);
        throw new UnsupportedOperationException("Get user not implemented yet");
    }

    // TODO: Implement email retrieval logic
    public String getUserEmail(UUID id) {
        log.info("Get user email by id: {}", id);
        throw new UnsupportedOperationException("Get user email not implemented yet");
    }
}

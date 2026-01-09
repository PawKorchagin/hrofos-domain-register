package ru.itmo.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.auth.dto.LoginRequest;
import ru.itmo.auth.dto.LoginResponse;
import ru.itmo.auth.dto.RegisterRequest;
import ru.itmo.auth.dto.UserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    // TODO: Implement registration logic
    public UserResponse register(RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());
        throw new UnsupportedOperationException("Registration not implemented yet");
    }

    // TODO: Implement login logic
    public LoginResponse login(LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        throw new UnsupportedOperationException("Login not implemented yet");
    }
}

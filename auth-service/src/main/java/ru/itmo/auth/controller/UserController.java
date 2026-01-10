package ru.itmo.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiError;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.auth.dto.UserResponse;
import ru.itmo.auth.service.UserService;
import ru.itmo.auth.util.SecurityUtil;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        UserResponse user ${DB_USER:***REMOVED***} userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse user ${DB_USER:***REMOVED***} userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable UUID id) {
        String email ${DB_USER:***REMOVED***} userService.getUserEmail(id);
        return ResponseEntity.ok(email);
    }
}

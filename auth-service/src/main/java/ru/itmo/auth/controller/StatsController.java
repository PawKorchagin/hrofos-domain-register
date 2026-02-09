package ru.itmo.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.auth.service.UserService;
import ru.itmo.common.dto.ApiResponse;

@RestController
@RequestMapping("/auth/stats")
@RequiredArgsConstructor
public class StatsController {

    private final UserService userService;

    @GetMapping("/users-count")
    public ResponseEntity<ApiResponse<Long>> getUsersCount() {
        long count ${DB_USER:***REMOVED***} userService.getUserCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}

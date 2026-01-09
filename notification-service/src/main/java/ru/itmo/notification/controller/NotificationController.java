package ru.itmo.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.common.notification.SendNotificationRequest;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendNotification(
            @RequestBody SendNotificationRequest request) {
        // TODO: Implement notification sending logic
        // - Validate request
        // - Get user email from Auth Service
        // - Send email via SMTP/Email API
        // - Log notification status
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(null));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Notification Service is running"));
    }
}

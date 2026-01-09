package ru.itmo.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.notification.dto.NotificationPreferencesResponse;
import ru.itmo.notification.dto.UpdateNotificationPreferencesRequest;

import java.util.UUID;

@RestController
@RequestMapping("/notifications/preferences")
@RequiredArgsConstructor
public class NotificationPreferencesController {

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationPreferencesResponse>> getPreferences(
            @RequestHeader("X-User-Id") UUID userId) {
        // TODO: Implement get preferences logic
        // - Get user preferences from database
        // - Return current notification settings
        NotificationPreferencesResponse response ${DB_USER:***REMOVED***} new NotificationPreferencesResponse();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<NotificationPreferencesResponse>> updatePreferences(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody UpdateNotificationPreferencesRequest request) {
        // TODO: Implement update preferences logic
        // - Validate request
        // - Update user preferences in database
        // - Return updated preferences
        NotificationPreferencesResponse response ${DB_USER:***REMOVED***} new NotificationPreferencesResponse();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

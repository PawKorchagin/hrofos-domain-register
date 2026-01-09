package ru.itmo.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.auth.dto.Enable2FAWithSecretRequest;
import ru.itmo.auth.dto.TwoFactorSetupResponse;
import ru.itmo.auth.service.TwoFactorService;
import ru.itmo.auth.util.SecurityUtil;
import ru.itmo.common.dto.ApiResponse;

import java.util.UUID;

@RestController
@RequestMapping("/auth/2fa")
@RequiredArgsConstructor
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @GetMapping("/setup")
    public ResponseEntity<ApiResponse<TwoFactorSetupResponse>> generateSetup() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).body(ApiResponse.error(
                    new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User must be authenticated")));
        }

        TwoFactorSetupResponse response ${DB_USER:***REMOVED***} twoFactorService.generateSetup(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/enable")
    public ResponseEntity<ApiResponse<String>> enable2FA(@Valid @RequestBody Enable2FAWithSecretRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).body(ApiResponse.error(
                    new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User must be authenticated")));
        }

        twoFactorService.enable2FA(userId, request.getSecret(), request);
        return ResponseEntity.ok(ApiResponse.success("2FA enabled successfully"));
    }

    @PostMapping("/disable")
    public ResponseEntity<ApiResponse<String>> disable2FA() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).body(ApiResponse.error(
                    new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User must be authenticated")));
        }

        twoFactorService.disable2FA(userId);
        return ResponseEntity.ok(ApiResponse.success("2FA disabled successfully"));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Boolean>> get2FAStatus() {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(401).body(ApiResponse.error(
                    new ru.itmo.common.dto.ApiError("UNAUTHORIZED", "User must be authenticated")));
        }

        boolean enabled ${DB_USER:***REMOVED***} twoFactorService.is2FAEnabled(userId);
        return ResponseEntity.ok(ApiResponse.success(enabled));
    }
}

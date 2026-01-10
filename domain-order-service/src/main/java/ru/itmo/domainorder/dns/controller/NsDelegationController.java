package ru.itmo.domainorder.dns.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.dns.dto.NsDelegationRequest;
import ru.itmo.domainorder.dns.dto.NsDelegationResponse;

import java.util.UUID;

@RestController
@RequestMapping("/dns/delegation")
@RequiredArgsConstructor
public class NsDelegationController {

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<NsDelegationResponse>> getDelegation(
            @PathVariable UUID domainId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<NsDelegationResponse>> setDelegation(
            @PathVariable UUID domainId,
            @Valid @RequestBody NsDelegationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<Void>> removeDelegation(@PathVariable UUID domainId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("NS Delegation Service is running"));
    }
}

package ru.itmo.domainorder.dns.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.dns.dto.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dns/records")
@RequiredArgsConstructor
public class DnsRecordController {

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<List<DnsRecordResponse>>> getRecordsByDomain(
            @PathVariable UUID domainId) {
        return ResponseEntity.ok(ApiResponse.success(List.of()));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> getRecordById(
            @PathVariable UUID recordId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/a")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createARecord(
            @Valid @RequestBody CreateARecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/aaaa")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createAaaaRecord(
            @Valid @RequestBody CreateAaaaRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/cname")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createCnameRecord(
            @Valid @RequestBody CreateCnameRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/txt")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createTxtRecord(
            @Valid @RequestBody CreateTxtRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/mx")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createMxRecord(
            @Valid @RequestBody CreateMxRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/srv")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createSrvRecord(
            @Valid @RequestBody CreateSrvRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/caa")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createCaaRecord(
            @Valid @RequestBody CreateCaaRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> updateRecord(
            @PathVariable UUID recordId,
            @Valid @RequestBody UpdateDnsRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable UUID recordId) {
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("DNS Service is running"));
    }
}

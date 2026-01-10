package ru.itmo.domainorder.dns.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiError;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.dns.dto.*;
import ru.itmo.domainorder.dns.service.DnsRecordService;
import ru.itmo.domainorder.util.SecurityUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dns/records")
@RequiredArgsConstructor
public class DnsRecordController {

    private final DnsRecordService dnsRecordService;

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<ApiResponse<List<DnsRecordResponse>>> getRecordsByDomain(
            @PathVariable UUID domainId) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        List<DnsRecordResponse> records ${DB_USER:***REMOVED***} dnsRecordService.getRecordsByDomain(domainId, userId);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> getRecordById(
            @PathVariable UUID recordId) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.getRecordById(recordId, userId);
        return ResponseEntity.ok(ApiResponse.success(record));
    }

    @PostMapping("/a")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createARecord(
            @Valid @RequestBody CreateARecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createARecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/aaaa")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createAaaaRecord(
            @Valid @RequestBody CreateAaaaRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createAaaaRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/cname")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createCnameRecord(
            @Valid @RequestBody CreateCnameRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createCnameRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/txt")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createTxtRecord(
            @Valid @RequestBody CreateTxtRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createTxtRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/mx")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createMxRecord(
            @Valid @RequestBody CreateMxRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createMxRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/srv")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createSrvRecord(
            @Valid @RequestBody CreateSrvRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createSrvRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PostMapping("/caa")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> createCaaRecord(
            @Valid @RequestBody CreateCaaRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.createCaaRecord(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(record));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<ApiResponse<DnsRecordResponse>> updateRecord(
            @PathVariable UUID recordId,
            @Valid @RequestBody UpdateDnsRecordRequest request) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        DnsRecordResponse record ${DB_USER:***REMOVED***} dnsRecordService.updateRecord(recordId, request, userId);
        return ResponseEntity.ok(ApiResponse.success(record));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable UUID recordId) {
        UUID userId ${DB_USER:***REMOVED***} SecurityUtil.getCurrentUserId();
        if (userId ${DB_USER:***REMOVED***}${DB_USER:***REMOVED***} null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(new ApiError("UNAUTHORIZED", "User not authenticated")));
        }
        dnsRecordService.deleteRecord(recordId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("DNS Service is running"));
    }
}

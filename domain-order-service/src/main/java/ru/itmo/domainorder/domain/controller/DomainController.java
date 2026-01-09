package ru.itmo.domainorder.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.domain.dto.CreateDomainRequest;
import ru.itmo.domainorder.domain.dto.DomainResponse;
import ru.itmo.domainorder.domain.dto.DomainSearchResult;
import ru.itmo.domainorder.domain.dto.UpdateDomainRequest;
import ru.itmo.domainorder.domain.service.DomainService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/domains")
@RequiredArgsConstructor
public class DomainController {
    private final DomainService domainService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DomainResponse>>> getAllDomains(Pageable pageable) {
        Page<DomainResponse> domains ${DB_USER:***REMOVED***} domainService.getAllDomains(pageable);
        return ResponseEntity.ok(ApiResponse.success(domains));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DomainResponse>> getDomainById(@PathVariable UUID id) {
        DomainResponse domain ${DB_USER:***REMOVED***} domainService.getDomainById(id);
        return ResponseEntity.ok(ApiResponse.success(domain));
    }

    @GetMapping("/fqdn/{fqdn}")
    public ResponseEntity<ApiResponse<DomainResponse>> getDomainByFqdn(@PathVariable String fqdn) {
        DomainResponse domain ${DB_USER:***REMOVED***} domainService.getDomainByFqdn(fqdn);
        return ResponseEntity.ok(ApiResponse.success(domain));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DomainResponse>> createDomain(
            @Valid @RequestBody CreateDomainRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        DomainResponse domain ${DB_USER:***REMOVED***} domainService.createDomain(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(domain));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DomainResponse>> updateDomain(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDomainRequest request) {
        DomainResponse domain ${DB_USER:***REMOVED***} domainService.updateDomain(id, request);
        return ResponseEntity.ok(ApiResponse.success(domain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDomain(@PathVariable UUID id) {
        domainService.deleteDomain(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<DomainResponse>> activateDomain(@PathVariable UUID id) {
        DomainResponse domain ${DB_USER:***REMOVED***} domainService.activateDomain(id);
        return ResponseEntity.ok(ApiResponse.success(domain));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DomainSearchResult>>> searchDomains(
            @RequestParam("query") String query) {
        List<DomainSearchResult> results ${DB_USER:***REMOVED***} domainService.searchDomains(query);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}

package ru.itmo.domainorder.zone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.domainorder.util.SecurityUtil;
import ru.itmo.domainorder.zone.dto.CreateZoneRequest;
import ru.itmo.domainorder.zone.dto.UpdateZoneRequest;
import ru.itmo.domainorder.zone.entity.Zone;
import ru.itmo.domainorder.zone.service.ZoneService;

import java.util.UUID;

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Zone>>> getAllZones(Pageable pageable) {
        Page<Zone> zones ${DB_USER:***REMOVED***} zoneService.getAllZones(pageable);
        return ResponseEntity.ok(ApiResponse.success(zones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Zone>> getZoneById(@PathVariable UUID id) {
        Zone zone ${DB_USER:***REMOVED***} zoneService.getZoneById(id);
        return ResponseEntity.ok(ApiResponse.success(zone));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<Zone>> getZoneByName(@PathVariable String name) {
        Zone zone ${DB_USER:***REMOVED***} zoneService.getZoneByName(name);
        return ResponseEntity.ok(ApiResponse.success(zone));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Zone>> createZone(@Valid @RequestBody CreateZoneRequest request) {
        if (!SecurityUtil.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("FORBIDDEN", "Admin access required")));
        }
        Zone zone ${DB_USER:***REMOVED***} zoneService.createZone(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(zone));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Zone>> updateZone(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateZoneRequest request) {
        if (!SecurityUtil.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("FORBIDDEN", "Admin access required")));
        }
        Zone zone ${DB_USER:***REMOVED***} zoneService.updateZone(id, request);
        return ResponseEntity.ok(ApiResponse.success(zone));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable UUID id) {
        if (!SecurityUtil.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(new ru.itmo.common.dto.ApiError("FORBIDDEN", "Admin access required")));
        }
        zoneService.deleteZone(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}


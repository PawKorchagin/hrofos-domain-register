package ru.itmo.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.common.dto.ApiResponse;
import ru.itmo.notification.dto.DomainExpiryAlertResponse;
import ru.itmo.notification.dto.DomainExpiryMonitoringResponse;
import ru.itmo.notification.dto.MonitoringDashboardResponse;
import ru.itmo.notification.dto.UpdateExpiryAlertsRequest;

import java.util.UUID;

@RestController
@RequestMapping("/notifications/expiry")
@RequiredArgsConstructor
public class DomainExpiryController {

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<DomainExpiryAlertResponse>> getExpiryAlerts(
            @RequestHeader("X-User-Id") UUID userId) {
        // TODO: Implement get expiry alerts logic
        // - Get user's expiry alert preferences from database
        // - Return current alert settings (days_before, enabled)
        DomainExpiryAlertResponse response ${DB_USER:***REMOVED***} new DomainExpiryAlertResponse();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/alerts")
    public ResponseEntity<ApiResponse<DomainExpiryAlertResponse>> updateExpiryAlerts(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody UpdateExpiryAlertsRequest request) {
        // TODO: Implement update expiry alerts logic
        // - Validate request (days_before must be >${DB_USER:***REMOVED***} 0)
        // - Update expiry_email_pref table
        // - Support default periods: 30, 14, 7, 3, 1 day
        // - Support custom periods
        // - Return updated preferences
        DomainExpiryAlertResponse response ${DB_USER:***REMOVED***} new DomainExpiryAlertResponse();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/monitoring/domains")
    public ResponseEntity<ApiResponse<Page<DomainExpiryMonitoringResponse>>> getMonitoringDomains(
            @RequestHeader("X-User-Id") UUID userId,
            Pageable pageable) {
        // TODO: Implement get monitoring domains logic
        // - Get user's domains from Domain Service
        // - Filter by expiry date based on user preferences
        // - Return list of domains with expiry information
        return ResponseEntity.ok(ApiResponse.success(Page.empty()));
    }

    @GetMapping("/monitoring/dashboard")
    public ResponseEntity<ApiResponse<MonitoringDashboardResponse>> getMonitoringDashboard(
            @RequestHeader("X-User-Id") UUID userId) {
        // TODO: Implement get monitoring dashboard logic
        // - Get statistics about expiring domains
        // - Count domains expiring in 30, 14, 7, 3, 1 days
        // - Count expired domains
        // - Return dashboard data
        MonitoringDashboardResponse response ${DB_USER:***REMOVED***} new MonitoringDashboardResponse();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

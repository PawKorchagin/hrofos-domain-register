package ru.itmo.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.admin.generated.api.HealthApi;
import ru.itmo.admin.generated.model.HealthResponse;

@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.adminService.base-path:/admin}")
public class HealthApiController implements HealthApi {

    @Override
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse response ${DB_USER:***REMOVED***} new HealthResponse();
        response.setStatus("UP");
        return ResponseEntity.ok(response);
    }
}

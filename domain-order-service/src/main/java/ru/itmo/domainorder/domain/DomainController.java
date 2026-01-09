package ru.itmo.domainorder.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/domains")
public class DomainController {
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getDomains() {
        return ResponseEntity.ok(ApiResponse.success(List.of("example.zone.ru", "test.zone.ru")));
    }
}

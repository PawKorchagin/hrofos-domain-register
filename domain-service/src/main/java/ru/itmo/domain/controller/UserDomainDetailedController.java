package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.dto.UserDomainDetailedResponse;
import ru.itmo.domain.service.UserDomainService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/domains")
public class UserDomainDetailedController {

    private final UserDomainService userDomainService;

    @GetMapping("/userDomains/detailed")
    public ResponseEntity<List<UserDomainDetailedResponse>> getUserDomainsDetailed() {
        List<UserDomainDetailedResponse> domains ${DB_USER:***REMOVED***} userDomainService.getUserDomainsDetailed();
        return ResponseEntity.ok(domains);
    }
}

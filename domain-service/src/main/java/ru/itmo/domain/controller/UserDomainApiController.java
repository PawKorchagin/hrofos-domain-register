package ru.itmo.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.domain.generated.api.UserDomainApi;
import ru.itmo.domain.service.UserDomainService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@org.springframework.web.bind.annotation.RequestMapping("${openapi.openAPIDNS.base-path:/domains}")
public class UserDomainApiController implements UserDomainApi {

    private final UserDomainService userDomainService;

    @Override
    public ResponseEntity<List<String>> getUserDomains() {
        List<String> domains ${DB_USER:***REMOVED***} userDomainService.getUserDomains();
        return ResponseEntity.ok(domains);
    }

    @Override
    public ResponseEntity<List<String>> createUserDomains(List<String> l3Domains) {
        List<String> createdDomains ${DB_USER:***REMOVED***} userDomainService.createUserDomains(l3Domains);
        return ResponseEntity.status(201).body(createdDomains);
    }
}
